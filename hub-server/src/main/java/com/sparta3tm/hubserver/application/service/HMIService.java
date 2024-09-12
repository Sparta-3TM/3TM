package com.sparta3tm.hubserver.application.service;

import com.sparta3tm.common.support.error.CoreApiException;
import com.sparta3tm.common.support.error.ErrorType;
import com.sparta3tm.hubserver.application.dto.hmi.AddUpdateHMIDto;
import com.sparta3tm.hubserver.application.dto.hmi.RemoveUpdateHMIDto;
import com.sparta3tm.hubserver.application.dto.hmi.RequestHMIDto;
import com.sparta3tm.hubserver.application.dto.hmi.ResponseHMIDto;
import com.sparta3tm.hubserver.domain.entity.HubMovementInfo;
import com.sparta3tm.hubserver.domain.repository.HMIRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class HMIService {

    private final HMIRepository hmiRepository;
    private final HubService hubService;

    @Transactional
    public ResponseHMIDto createHmi(RequestHMIDto requestHMIDto) {
        hubService.searchHubById(requestHMIDto.startHub());
        hubService.searchHubById(requestHMIDto.endHub());
        List<Long> list = requestHMIDto.transitHubId();
        for (Long l : list) hubService.searchHubById(l);
        return ResponseHMIDto.of(hmiRepository.save(connectionHmi(requestHMIDto, list)));
    }

    @Transactional(readOnly = true)
    @Cacheable(cacheNames = "hmi_cache", key = "args[0]")
    public ResponseHMIDto searchHmiById(Long hmiId) {
        return ResponseHMIDto.of(hmiRepository.findByIdAndIsDeleteFalse(hmiId).orElseThrow(() -> new CoreApiException(ErrorType.NOT_FOUND_ERROR)));
    }

    @Transactional
    @CachePut(cacheNames = "hmi_cache", key = "args[0]")
    public ResponseHMIDto addUpdateHmi(Long hmiId, AddUpdateHMIDto addUpdateHMIDto) {
        HubMovementInfo hmi = hmiRepository.findByIdAndIsDeleteFalse(hmiId).orElseThrow(() -> new CoreApiException(ErrorType.NOT_FOUND_ERROR));
        hubService.searchHubById(hmiId);
        hubService.searchHubById(addUpdateHMIDto.addHubId());
        if (hmi.getParentMovementInfo() != null) throw new CoreApiException(ErrorType.BAD_REQUEST);
        return updateConnectionAddHmi(hmi, addUpdateHMIDto);
    }

    @Transactional
    @CachePut(cacheNames = "hmi_cache", key = "args[0]")
    public ResponseHMIDto removeUpdateHmi(Long hmiId, RemoveUpdateHMIDto removeUpdateHMIDto) {
        HubMovementInfo hmi = hmiRepository.findByIdAndIsDeleteFalse(hmiId).orElseThrow(() -> new CoreApiException(ErrorType.NOT_FOUND_ERROR));
        if (hmi.getParentMovementInfo() != null) throw new CoreApiException(ErrorType.BAD_REQUEST);
        return updateConnectionRemoveHmi(hmi, removeUpdateHMIDto);
    }

    @Transactional
    @CacheEvict(cacheNames = "hmi_cache", key = "args[0]")
    public void deleteHmi(Long hmiId, String username) {
        HubMovementInfo hmi = hmiRepository.findByIdAndIsDeleteFalse(hmiId).orElseThrow(() -> new CoreApiException(ErrorType.NOT_FOUND_ERROR));
        hmi.softDelete(username);
    }

    // TODO: 추후 naver api 를 통해 외부 변수들을 고려하여 estimatedTime 과 estimatedDistance 값을 변경하도록 생각해야 할 듯 ( addSubMovement 부분에서.. )

    private static HubMovementInfo connectionHmi(RequestHMIDto requestHMIDto, List<Long> list) {
        int index = 0;
        HubMovementInfo hmi = new HubMovementInfo(requestHMIDto.startHub(), requestHMIDto.endHub(), requestHMIDto.estimatedTime(), requestHMIDto.estimatedDistance());
        HubMovementInfo sub = new HubMovementInfo(requestHMIDto.startHub(), list.get(0), requestHMIDto.estimatedTime(), requestHMIDto.estimatedDistance());
        sub.addIndex(index++);
        hmi.addSubMovement(sub);
        for (int i = 1; i < list.size(); i++) {
            HubMovementInfo subHmi = new HubMovementInfo(list.get(i - 1), requestHMIDto.transitHubId().get(i), requestHMIDto.estimatedTime(), requestHMIDto.estimatedDistance());
            subHmi.addIndex(index++);
            hmi.addSubMovement(subHmi);
        }
        HubMovementInfo subHmi = new HubMovementInfo(list.get(list.size() - 1), requestHMIDto.endHub(), requestHMIDto.estimatedTime(), requestHMIDto.estimatedDistance());
        subHmi.addIndex(index);
        hmi.addSubMovement(subHmi);
        return hmi;
    }


    private ResponseHMIDto updateConnectionAddHmi(HubMovementInfo hmi, AddUpdateHMIDto addUpdateHMIDto) {
        int position = addUpdateHMIDto.position();
        int subHmiSize = hmi.getSubMovementInfo().size();
        Long addHubId = addUpdateHMIDto.addHubId();
        List<HubMovementInfo> list = hmi.getSubMovementInfo();

        if (list.isEmpty()) {
            log.info("List is Empty!");
            int index = 0;
            if (position == 0) {
                HubMovementInfo sub1 = new HubMovementInfo(addHubId, hmi.getStartHub(), hmi.getEstimatedTime(), hmi.getEstimatedDistance());
                HubMovementInfo sub2 = new HubMovementInfo(hmi.getStartHub(), hmi.getEndHub(), hmi.getEstimatedTime(), hmi.getEstimatedDistance());
                sub1.addIndex(index++);
                sub2.addIndex(index);
                hmi.addSubMovement(sub1);
                hmi.addSubMovement(sub2);
                hmi.updateStartHub(addHubId);
            } else if (position == 2) {
                HubMovementInfo sub1 = new HubMovementInfo(hmi.getStartHub(), hmi.getEndHub(), hmi.getEstimatedTime(), hmi.getEstimatedDistance());
                HubMovementInfo sub2 = new HubMovementInfo(hmi.getEndHub(), addHubId, hmi.getEstimatedTime(), hmi.getEstimatedDistance());
                sub1.addIndex(index++);
                sub2.addIndex(index);
                hmi.addSubMovement(sub1);
                hmi.addSubMovement(sub2);
                hmi.updateEndHub(addHubId);
            } else {
                HubMovementInfo sub1 = new HubMovementInfo(hmi.getStartHub(), addHubId, hmi.getEstimatedTime(), hmi.getEstimatedDistance());
                HubMovementInfo sub2 = new HubMovementInfo(addHubId, hmi.getEndHub(), hmi.getEstimatedTime(), hmi.getEstimatedDistance());
                sub1.addIndex(index++);
                sub2.addIndex(index);
                hmi.addSubMovement(sub1);
                hmi.addSubMovement(sub2);
            }
        } else if (position == 0) {
            log.info("start hub add update");
            HubMovementInfo sub = new HubMovementInfo(addHubId, hmi.getStartHub(), hmi.getEstimatedTime(), hmi.getEstimatedDistance());
            list.forEach(info -> info.addIndex(info.getIndex() + 1));
            sub.addIndex(position);
            hmi.addSubMovement(sub);
            hmi.updateStartHub(addHubId);

        } else if (position == subHmiSize + 1) {
            log.info("end hub add update");

            HubMovementInfo sub = new HubMovementInfo(hmi.getEndHub(), addHubId, hmi.getEstimatedTime(), hmi.getEstimatedDistance());
            sub.addIndex(list.size());
            hmi.addSubMovement(sub);
            hmi.updateEndHub(addHubId);

        } else if (0 < position && position <= subHmiSize) {
            log.info("middle hub add update");
            HubMovementInfo sub = list.get(position - 1);
            HubMovementInfo subNew = new HubMovementInfo(sub.getStartHub(), addHubId, sub.getEstimatedTime(), sub.getEstimatedDistance());
            sub.updateStartHub(addHubId);

            list.forEach(info -> {
                if (info.getIndex() >= position - 1) info.addIndex(info.getIndex() + 1);
            });
            subNew.addIndex(position - 1);
            hmi.addSubMovement(subNew);

        } else throw new CoreApiException(ErrorType.BAD_REQUEST);

        return ResponseHMIDto.of(hmiRepository.save(hmi));
    }

    private ResponseHMIDto updateConnectionRemoveHmi(HubMovementInfo hmi, RemoveUpdateHMIDto removeUpdateHMIDto) {
        List<HubMovementInfo> list = hmi.getSubMovementInfo();
        Long removeHubId = removeUpdateHMIDto.removeHubId();

        if (list.isEmpty()) {
            throw new CoreApiException(ErrorType.BAD_REQUEST);
        } else if (removeHubId.equals(hmi.getStartHub())) {
            log.info("start hub remove update");
            if (list.size() == 2) {
                HubMovementInfo sub = list.get(0);
                hmi.updateStartHub(sub.getEndHub());
                list.clear();
            } else {
                HubMovementInfo removeHmi = list.stream()
                        .filter(info -> info.getStartHub().equals(removeHubId))
                        .findFirst()
                        .orElseThrow(() -> new CoreApiException(ErrorType.BAD_REQUEST));

                hmi.updateStartHub(removeHmi.getEndHub());
                hmi.removeSubMovement(removeHmi);
                list.forEach(info -> info.addIndex(info.getIndex() - 1));
            }
        } else if (removeHubId.equals(hmi.getEndHub())) {
            log.info("end hub remove update");
            if (list.size() == 2) {
                HubMovementInfo sub = list.get(list.size() - 1);
                hmi.updateEndHub(sub.getStartHub());
                list.clear();
            } else {
                HubMovementInfo removeHmi = list.stream()
                        .filter(info -> info.getEndHub().equals(removeHubId))
                        .findFirst()
                        .orElseThrow(() -> new CoreApiException(ErrorType.BAD_REQUEST));
                hmi.updateEndHub(removeHmi.getStartHub());
                hmi.removeSubMovement(removeHmi);
            }
        } else {
            log.info("middle hub remove update");
            if (list.size() == 2) {
                list.clear();
            } else {
                HubMovementInfo removeHmi = list.stream()
                        .filter(info -> info.getStartHub().equals(removeHubId))
                        .findFirst()
                        .orElseThrow(() -> new CoreApiException(ErrorType.BAD_REQUEST));

                Integer index = removeHmi.getIndex();
                HubMovementInfo sub = list.get(index - 1);
                sub.updateEndHub(removeHmi.getEndHub());
                hmi.removeSubMovement(removeHmi);
                list.forEach(info -> {
                    if (info.getIndex() > index) info.addIndex(info.getIndex() - 1);
                });
            }
        }
        return ResponseHMIDto.of(hmiRepository.save(hmi));
    }


    /** @Transactional(readOnly = true)
     * 솔직히 페이징 조회가 이 복잡한 Hmi 에서 의미가 있나 싶음 없애는게 낫다고 판단.
    public ResponsePageHMIDto searchHmi(Pageable pageable) {
    Page<HubMovementInfo> list = hmiRepository.findAllByIsDeleteFalse(pageable);
    return ResponsePageHMIDto.of(list.stream().map(ResponseHMIDto::of).toList(), list.hasNext());
    }*/
}
