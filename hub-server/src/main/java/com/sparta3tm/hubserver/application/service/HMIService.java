package com.sparta3tm.hubserver.application.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sparta3tm.common.support.error.CoreApiException;
import com.sparta3tm.common.support.error.ErrorType;
import com.sparta3tm.hubserver.application.dto.hmi.request.AddUpdateHMIDto;
import com.sparta3tm.hubserver.application.dto.hmi.request.RemoveUpdateHMIDto;
import com.sparta3tm.hubserver.application.dto.hmi.request.RequestHMIDto;
import com.sparta3tm.hubserver.application.dto.hmi.response.ResponseHMIDto;
import com.sparta3tm.hubserver.application.dto.hub.response.ResponseHubDto;
import com.sparta3tm.hubserver.application.dto.hub.response.ResponseHubManagerDto;
import com.sparta3tm.hubserver.domain.entity.Hub;
import com.sparta3tm.hubserver.domain.entity.HubMovementInfo;
import com.sparta3tm.hubserver.domain.repository.HMIRepository;
import com.sparta3tm.hubserver.infrastructure.client.OrderClient;
import com.sparta3tm.hubserver.infrastructure.client.dto.DeliveryUpdateHubDto;
import com.sparta3tm.hubserver.infrastructure.naver.dto.StopoverDto;
import com.sparta3tm.hubserver.infrastructure.naver.service.NaverService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class HMIService {

    private final HMIRepository hmiRepository;
    private final HubService hubService;
    private final OrderClient orderClient;
    private final NaverService naverService;

    @Transactional
    public ResponseHMIDto createHmi(RequestHMIDto requestHMIDto) {
        ResponseHubDto startHub = hubService.searchHubById(requestHMIDto.startHub());
        ResponseHubDto endHub = hubService.searchHubById(requestHMIDto.endHub());

        String start = startHub.longitude() + "," + startHub.latitude();
        String end = endHub.longitude() + "," + endHub.latitude();

        List<ResponseHubDto> stopoverList = new ArrayList<>();
        for (Long stopover : requestHMIDto.transitHubId()) stopoverList.add(hubService.searchHubById(stopover));

        StringBuilder stopoverHubList = new StringBuilder();
        for (ResponseHubDto stopoverHub : stopoverList)
            stopoverHubList.append(stopoverHub.longitude().toString()).append(",").append(stopoverHub.latitude().toString()).append("|");
        String sub = stopoverHubList.substring(0, stopoverHubList.length() - 1);

        try {
            List<StopoverDto> stopoverInfoList = naverService.naverApi(new String[]{start, end, sub}, stopoverList.size());
            return ResponseHMIDto.of(hmiRepository.save(connectionHmi(requestHMIDto, startHub.address(), stopoverInfoList, stopoverList.size())));
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
            throw new CoreApiException(ErrorType.DEFAULT_ERROR);
        }
    }

    @Transactional(readOnly = true)
    @Cacheable(cacheNames = "hmi_cache", key = "args[0]")
    public ResponseHMIDto searchHmiById(Long hmiId) {
        return ResponseHMIDto.of(hmiRepository.findByIdAndIsDeleteFalse(hmiId).orElseThrow(() -> new CoreApiException(ErrorType.NOT_FOUND_ERROR)));
    }

    @Transactional
    @CachePut(cacheNames = "hmi_cache", key = "args[0]")
    public ResponseHMIDto addUpdateHmi(Long hmiId, AddUpdateHMIDto addUpdateHMIDto, String userId, String userRole) {
        HubMovementInfo hmi = hmiRepository.findByIdAndIsDeleteFalse(hmiId).orElseThrow(() -> new CoreApiException(ErrorType.NOT_FOUND_ERROR));
        if (hmi.getParentMovementInfo() != null) throw new CoreApiException(ErrorType.BAD_REQUEST);
        if (hmi.getSubMovementInfo().size() == 6) throw new CoreApiException(ErrorType.BAD_REQUEST);
        // 경유지 허브는 최대 5개 => Naver Map Api 제약

        List<ResponseHubDto> list = new ArrayList<>();
        for (HubMovementInfo info : hmi.getSubMovementInfo()) list.add(hubService.searchHubById(info.getStartHub()));
        list.add(hubService.searchHubById(hmi.getEndHub()));

        ResponseHubDto addHub = hubService.searchHubById(addUpdateHMIDto.addHubId());
        list.add(addUpdateHMIDto.position(), addHub);

        String[] strings = requestNaverApi(list);
        try {
            List<StopoverDto> stopoverDtoList = naverService.naverApi(strings, list.size() - 2);
            ResponseHMIDto response = ResponseHMIDto.of(updateConnectionAddHmi(hmi, stopoverDtoList, addUpdateHMIDto));
            orderClient.updateDeliveryByHmi(hmiId, new DeliveryUpdateHubDto("IN_DELIVERY", response.startHub(), response.endHub(), response.address(), response.estimatedTime(), response.estimatedDistance()), userId, userRole);
            return response;
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new CoreApiException(ErrorType.DEFAULT_ERROR);
        }
    }

    @Transactional
    @CachePut(cacheNames = "hmi_cache", key = "args[0]")
    public ResponseHMIDto removeUpdateHmi(Long hmiId, RemoveUpdateHMIDto removeUpdateHMIDto, String userId, String userRole) {
        HubMovementInfo hmi = hmiRepository.findByIdAndIsDeleteFalse(hmiId).orElseThrow(() -> new CoreApiException(ErrorType.NOT_FOUND_ERROR));
        if (hmi.getParentMovementInfo() != null) throw new CoreApiException(ErrorType.BAD_REQUEST);

        List<ResponseHubDto> list = new ArrayList<>();
        List<HubMovementInfo> subList = hmi.getSubMovementInfo();
        if (subList.isEmpty()) throw new CoreApiException(ErrorType.BAD_REQUEST);

        for (HubMovementInfo info : subList)
            if (!info.getStartHub().equals(removeUpdateHMIDto.removeHubId()))
                list.add(hubService.searchHubById(info.getStartHub()));
        if (!hmi.getEndHub().equals(removeUpdateHMIDto.removeHubId()))
            list.add(hubService.searchHubById(hmi.getEndHub()));

        try {
            List<StopoverDto> stopoverDtoList = naverService.naverApi(requestNaverApi(list), list.size() - 2);
            ResponseHMIDto response = updateConnectionRemoveHmi(hmi, stopoverDtoList, removeUpdateHMIDto);
            orderClient.updateDeliveryByHmi(hmiId, new DeliveryUpdateHubDto("IN_DELIVERY", response.startHub(), response.endHub(), response.address(), response.estimatedTime(), response.estimatedDistance()), userId, userRole);
            return response;
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new CoreApiException(ErrorType.DEFAULT_ERROR);
        }
    }

    @Transactional
    @CacheEvict(cacheNames = "hmi_cache", key = "args[0]")
    public void deleteHmi(Long hmiId, String username, String userRole) {
        HubMovementInfo hmi = hmiRepository.findByIdAndIsDeleteFalse(hmiId).orElseThrow(() -> new CoreApiException(ErrorType.NOT_FOUND_ERROR));
        if (!userRole.equals("MASTER")) throw new CoreApiException(ErrorType.BAD_REQUEST);
        if (hmi.getParentMovementInfo() == null) throw new CoreApiException(ErrorType.BAD_REQUEST);
        hmi.softDelete(username);
    }

    private String[] requestNaverApi(List<ResponseHubDto> list) {
        String start = list.getFirst().longitude() + "," + list.getFirst().latitude();
        String end = list.getLast().longitude() + "," + list.getLast().latitude();

        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 1; i < list.size() - 1; i++)
            stringBuilder.append(list.get(i).longitude().toString()).append(",").append(list.get(i).latitude().toString()).append("|");
        String sub = stringBuilder.substring(0, stringBuilder.length() - 1);
        return new String[]{start, end, sub};
    }


    private HubMovementInfo connectionHmi(RequestHMIDto requestHMIDto, String startAddress, List<StopoverDto> stopoverInfoList, int stopoverNum) {
        int index = 0;
        List<Long> list = requestHMIDto.transitHubId();

        if (list.isEmpty())
            return new HubMovementInfo(requestHMIDto.startHub(), requestHMIDto.endHub(), convertDoubleToLocalTime(stopoverInfoList.getFirst().duration()), stopoverInfoList.getFirst().distance());

        HubMovementInfo hmi = new HubMovementInfo(requestHMIDto.startHub(), requestHMIDto.endHub(), convertDoubleToLocalTime(stopoverInfoList.getLast().duration()), stopoverInfoList.getLast().distance());
        HubMovementInfo sub = new HubMovementInfo(requestHMIDto.startHub(), list.getFirst(), convertDoubleToLocalTime(stopoverInfoList.getFirst().duration()), stopoverInfoList.getFirst().distance());
        hmi.updateAddress(startAddress);
        hmi.addSubMovement(sub);
        sub.addIndex(index++);

        for (int i = 1; i < list.size(); i++) {
            HubMovementInfo subHmi = new HubMovementInfo(list.get(i - 1), list.get(i), convertDoubleToLocalTime(stopoverInfoList.get(i).duration()), stopoverInfoList.get(i).distance());
            subHmi.addIndex(index++);
            hmi.addSubMovement(subHmi);
        }
        HubMovementInfo subHmi = new HubMovementInfo(list.getLast(), requestHMIDto.endHub(), convertDoubleToLocalTime(stopoverInfoList.get(stopoverInfoList.size() - 2).duration()), stopoverInfoList.get(stopoverInfoList.size() - 2).distance());


        subHmi.addIndex(index);
        hmi.addSubMovement(subHmi);
        return hmi;
    }


    private HubMovementInfo updateConnectionAddHmi(HubMovementInfo hmi, List<StopoverDto> stopoverDtoList, AddUpdateHMIDto addUpdateHMIDto) {
        int position = addUpdateHMIDto.position();
        int subHmiSize = hmi.getSubMovementInfo().size();
        Long addHubId = addUpdateHMIDto.addHubId();
        List<HubMovementInfo> list = hmi.getSubMovementInfo();

        if (list.isEmpty()) {
            log.info("List is Empty!");
            int index = 0;
            if (position == 0) {
                hmi.updateStartHub(addHubId);
                hmi.updateDistance(stopoverDtoList.getLast().distance());
                hmi.updateDuration(convertDoubleToLocalTime(stopoverDtoList.getLast().duration()));

                HubMovementInfo sub1 = new HubMovementInfo(addHubId, hmi.getStartHub(), convertDoubleToLocalTime(stopoverDtoList.getFirst().duration()), stopoverDtoList.getFirst().distance());
                HubMovementInfo sub2 = new HubMovementInfo(hmi.getStartHub(), hmi.getEndHub(), convertDoubleToLocalTime(stopoverDtoList.get(1).duration()), stopoverDtoList.get(1).distance());
                sub1.addIndex(index++);
                sub2.addIndex(index);
                hmi.addSubMovement(sub1);
                hmi.addSubMovement(sub2);
                hmi.updateAddress(hubService.searchHubById(addHubId).address());
            } else if (position == 2) {
                hmi.updateEndHub(addHubId);
                hmi.updateDistance(stopoverDtoList.getLast().distance());
                hmi.updateDuration(convertDoubleToLocalTime(stopoverDtoList.getLast().duration()));

                HubMovementInfo sub1 = new HubMovementInfo(hmi.getStartHub(), hmi.getEndHub(), convertDoubleToLocalTime(stopoverDtoList.getFirst().duration()), stopoverDtoList.getFirst().distance());
                HubMovementInfo sub2 = new HubMovementInfo(hmi.getEndHub(), addHubId, convertDoubleToLocalTime(stopoverDtoList.get(1).duration()), stopoverDtoList.get(1).distance());
                sub1.addIndex(index++);
                sub2.addIndex(index);
                hmi.addSubMovement(sub1);
                hmi.addSubMovement(sub2);
            } else {
                hmi.updateDistance(stopoverDtoList.getLast().distance());
                hmi.updateDuration(convertDoubleToLocalTime(stopoverDtoList.getLast().duration()));

                HubMovementInfo sub1 = new HubMovementInfo(hmi.getStartHub(), addHubId, convertDoubleToLocalTime(stopoverDtoList.getFirst().duration()), stopoverDtoList.getFirst().distance());
                HubMovementInfo sub2 = new HubMovementInfo(addHubId, hmi.getEndHub(), convertDoubleToLocalTime(stopoverDtoList.get(1).duration()), stopoverDtoList.get(1).distance());
                sub1.addIndex(index++);
                sub2.addIndex(index);
                hmi.addSubMovement(sub1);
                hmi.addSubMovement(sub2);
            }
        } else if (position == 0) {
            log.info("start hub add update");
            hmi.updateDistance(stopoverDtoList.getLast().distance());
            hmi.updateDuration(convertDoubleToLocalTime(stopoverDtoList.getLast().duration()));

            HubMovementInfo sub = new HubMovementInfo(addHubId, hmi.getStartHub(), convertDoubleToLocalTime(stopoverDtoList.getFirst().duration()), stopoverDtoList.getFirst().distance());
            hmi.updateStartHub(addHubId);
            for (int i = 0; i < list.size(); i++) {
                list.get(i).addIndex(list.get(i).getIndex() + 1);
                list.get(i).updateDuration(convertDoubleToLocalTime(stopoverDtoList.get(i + 1).duration()));
                list.get(i).updateDistance(stopoverDtoList.get(i + 1).distance());
            }

            sub.addIndex(position);
            hmi.addSubMovement(sub);
            hmi.updateAddress(hubService.searchHubById(addHubId).address());
        } else if (position == subHmiSize + 1) {
            log.info("end hub add update");
            hmi.updateDistance(stopoverDtoList.getLast().distance());
            hmi.updateDuration(convertDoubleToLocalTime(stopoverDtoList.getLast().duration()));

            for (int i = 0; i < stopoverDtoList.size() - 2; i++) {
                list.get(i).updateDuration(convertDoubleToLocalTime(stopoverDtoList.get(i).duration()));
                list.get(i).updateDistance(stopoverDtoList.get(i).distance());
            }
            HubMovementInfo sub = new HubMovementInfo(hmi.getEndHub(), addHubId, convertDoubleToLocalTime(stopoverDtoList.get(stopoverDtoList.size() - 2).duration()), stopoverDtoList.get(stopoverDtoList.size() - 2).distance());
            hmi.updateEndHub(addHubId);
            sub.addIndex(list.size());
            hmi.addSubMovement(sub);
        } else if (0 < position && position <= subHmiSize) {
            log.info("middle hub add update");
            hmi.updateDistance(stopoverDtoList.getLast().distance());
            hmi.updateDuration(convertDoubleToLocalTime(stopoverDtoList.getLast().duration()));

            HubMovementInfo sub = list.get(position - 1);
            HubMovementInfo addHub = new HubMovementInfo(addHubId, sub.getEndHub(), convertDoubleToLocalTime(stopoverDtoList.get(position).duration()), stopoverDtoList.get(position).distance());

            sub.updateEndHub(addHubId);
            addHub.addIndex(position);


            for (int i = position; i < list.size(); i++) list.get(i).addIndex(list.get(i).getIndex() + 1);
            stopoverDtoList.remove(position);

            for (int i = 0; i < list.size(); i++) {
                list.get(i).updateDistance(stopoverDtoList.get(i).distance());
                list.get(i).updateDuration(convertDoubleToLocalTime(stopoverDtoList.get(i).duration()));
            }
            hmi.addSubMovement(addHub);
        } else throw new CoreApiException(ErrorType.BAD_REQUEST);

        return hmiRepository.save(hmi);
    }

    private ResponseHMIDto updateConnectionRemoveHmi(HubMovementInfo hmi, List<StopoverDto> stopoverDtoList, RemoveUpdateHMIDto removeUpdateHMIDto) {
        List<HubMovementInfo> list = hmi.getSubMovementInfo();
        Long removeHubId = removeUpdateHMIDto.removeHubId();

        if (removeHubId.equals(hmi.getStartHub())) {
            log.info("start hub remove update");
            if (list.size() == 2) {
                HubMovementInfo sub = list.getFirst();
                hmi.removeSubMovement(sub);


                hmi.updateStartHub(sub.getEndHub());
                hmi.updateDistance(stopoverDtoList.getLast().distance());
                hmi.updateAddress(hubService.searchHubById(sub.getEndHub()).address());
                hmi.updateDuration(convertDoubleToLocalTime(stopoverDtoList.getLast().duration()));
                list.clear();
            } else {
                HubMovementInfo sub = list.stream()
                        .filter(info -> info.getStartHub().equals(removeHubId))
                        .findFirst()
                        .orElseThrow(() -> new CoreApiException(ErrorType.BAD_REQUEST));

                hmi.removeSubMovement(sub);
                for (int i = 0; i < stopoverDtoList.size() - 1; i++) {
                    list.get(i).updateDistance(stopoverDtoList.get(i).distance());
                    list.get(i).updateDuration(convertDoubleToLocalTime(stopoverDtoList.get(i).duration()));
                    list.get(i).addIndex(list.get(i).getIndex() - 1);
                }

                hmi.updateStartHub(sub.getEndHub());
                hmi.updateDistance(stopoverDtoList.getLast().distance());
                hmi.updateAddress(hubService.searchHubById(sub.getEndHub()).address());
                hmi.updateDuration(convertDoubleToLocalTime(stopoverDtoList.getLast().duration()));
            }
        } else if (removeHubId.equals(hmi.getEndHub())) {
            log.info("end hub remove update");
            if (list.size() == 2) {
                HubMovementInfo sub = list.getLast();
                hmi.updateEndHub(sub.getStartHub());
                hmi.updateDistance(stopoverDtoList.getLast().distance());
                hmi.updateDuration(convertDoubleToLocalTime(stopoverDtoList.getLast().duration()));

                list.clear();
            } else {
                HubMovementInfo sub = list.stream()
                        .filter(info -> info.getEndHub().equals(removeHubId))
                        .findFirst()
                        .orElseThrow(() -> new CoreApiException(ErrorType.BAD_REQUEST));

                hmi.removeSubMovement(sub);
                for (int i = 0; i < stopoverDtoList.size() - 1; i++) {
                    list.get(i).updateDistance(stopoverDtoList.get(i).distance());
                    list.get(i).updateDuration(convertDoubleToLocalTime(stopoverDtoList.get(i).duration()));
                }

                hmi.updateEndHub(sub.getStartHub());
                hmi.updateDistance(stopoverDtoList.getLast().distance());
                hmi.updateDuration(convertDoubleToLocalTime(stopoverDtoList.getLast().duration()));
            }
        } else {
            log.info("middle hub remove update");
            if (list.size() == 2) {
                hmi.updateDistance(stopoverDtoList.getLast().distance());
                hmi.updateDuration(convertDoubleToLocalTime(stopoverDtoList.getLast().duration()));

                list.clear();
            } else {
                HubMovementInfo removeHmi = list.stream()
                        .filter(info -> info.getStartHub().equals(removeHubId))
                        .findFirst()
                        .orElseThrow(() -> new CoreApiException(ErrorType.BAD_REQUEST));

                hmi.removeSubMovement(removeHmi);
                Integer index = removeHmi.getIndex();
                HubMovementInfo sub = list.get(index - 1);
                sub.updateEndHub(removeHmi.getEndHub());

                for (int i = 0; i < list.size(); i++) {
                    list.get(i).updateDistance(stopoverDtoList.get(i).distance());
                    list.get(i).updateDuration(convertDoubleToLocalTime(stopoverDtoList.get(i).duration()));
                    if (list.get(i).getIndex() > index) list.get(i).addIndex(list.get(i).getIndex() - 1);
                }
            }
        }
        return ResponseHMIDto.of(hmiRepository.save(hmi));
    }

    public List<ResponseHubManagerDto> searchHmiManager(Long hmiId, String userId, String userRole) {
        HubMovementInfo hmi = hmiRepository.findByIdAndIsDeleteFalse(hmiId).orElseThrow(() -> new CoreApiException(ErrorType.NOT_FOUND_ERROR));
        if (hmi.getParentMovementInfo() != null) throw new CoreApiException(ErrorType.BAD_REQUEST);

        List<Long> list = new ArrayList<>();
        hmi.getSubMovementInfo().forEach(sub -> list.add(sub.getStartHub()));
        list.add(hmi.getEndHub());
        List<Hub> hubs = hubService.searchHubByIdIn(list);
        List<ResponseHubManagerDto> responseList = new ArrayList<>();
        hubs.forEach(hub -> responseList.add(new ResponseHubManagerDto(hub.getManagerId())));

        return responseList;
    }

    public static LocalTime convertDoubleToLocalTime(Double time) {
        if (time == null) return null;
        int hours = (int) Math.floor(time);
        int minutes = (int) Math.round((time - hours) * 60);

        if (minutes == 60) minutes = 59;
        return LocalTime.of(hours, minutes);
    }

}
