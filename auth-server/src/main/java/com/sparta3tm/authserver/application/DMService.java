package com.sparta3tm.authserver.application;

import com.sparta3tm.authserver.application.dtos.DM.DMCreateReqDto;
import com.sparta3tm.authserver.application.dtos.DM.DMResDto;
import com.sparta3tm.authserver.application.dtos.DM.DMUpdateReqDto;
import com.sparta3tm.authserver.application.dtos.user.UserResDto;
import com.sparta3tm.authserver.domain.DM.DeliveryManager;
import com.sparta3tm.authserver.domain.DM.DeliveryManagerRepository;
import com.sparta3tm.authserver.domain.user.User;
import com.sparta3tm.authserver.domain.user.UserRepository;
import com.sparta3tm.common.support.error.CoreApiException;
import com.sparta3tm.common.support.error.ErrorType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j(topic = "DMService")
public class DMService {
    private final DeliveryManagerRepository deliveryManagerRepository;
    private final UserRepository userRepository;

    public DMResDto createDM(DMCreateReqDto dmCreateReqDto) {
        try{
            User user = userRepository.findById(dmCreateReqDto.getUserId()).orElseThrow();
            return DMResDto.from(deliveryManagerRepository.save(dmCreateReqDto.toEntity(user)));
        }catch (Exception e){
            log.error("존재하지 않는 User ID 입니다.");
            throw new CoreApiException(ErrorType.NOT_FOUND_ERROR);
        }
    }

    public DMResDto updateDM(Long deliveryManagersId, DMUpdateReqDto dmUpdateReqDto) {
        User user;
        try{
            user = userRepository.findById(dmUpdateReqDto.getUserId()).orElseThrow();
        }catch (Exception e){
            log.error("존재하지 않는 User ID 입니다.");
            throw new CoreApiException(ErrorType.NOT_FOUND_ERROR);
        }

        DeliveryManager deliveryManager = deliveryManagerRepository.findById(deliveryManagersId).orElseThrow(() -> {
            log.error("존재하지 않는 Delivery Manager ID 입니다.");
            throw new CoreApiException(ErrorType.NOT_FOUND_ERROR);
        });

        try{
            deliveryManager.updateDeliveryManagerInfo(
                    user,dmUpdateReqDto.getHubId(), dmUpdateReqDto.getSlackId(),
                    dmUpdateReqDto.getManagerType()
            );

            DeliveryManager savedDM = deliveryManagerRepository.save(deliveryManager);

            return DMResDto.from(savedDM);
        }catch (Exception e){
            log.error("INTERNAL SERVER ERROR");
            throw new CoreApiException(ErrorType.DEFAULT_ERROR);
        }
    }


    public String deleteDM(Long deliveryManagersId, String userId) {
        DeliveryManager deliveryManager = deliveryManagerRepository.findById(deliveryManagersId).orElseThrow(()->{
            log.error("존재하지 않는 Delivery Manager ID 입니다.");
            throw new CoreApiException(ErrorType.NOT_FOUND_ERROR);
        });

        try{
            deliveryManager.softDelete(userId);
            deliveryManagerRepository.save(deliveryManager);
        }catch (Exception e){
            log.error("INTERNAL SERVER ERROR");
            throw new CoreApiException(ErrorType.DEFAULT_ERROR);
        }

        return "DeliveryManager " + deliveryManager.getId() + " is Deleted";
    }

    public DMResDto getDeliveryManager(Long deliveryManagersId) {
        return DMResDto.from(deliveryManagerRepository.findById(deliveryManagersId).orElseThrow(()->{
            log.error("존재하지 않는 Delivery Manager ID 입니다.");
            throw new CoreApiException(ErrorType.NOT_FOUND_ERROR);
        }));
    }


    public List<DMResDto> searchDM(String keyword, Pageable pageable) {
        try{
            if(keyword == null){
                return deliveryManagerRepository.findAllBySlackIdContaining("", pageable).stream().map(DMResDto::from).toList();
            }
            return deliveryManagerRepository.findAllBySlackIdContaining(keyword, pageable).stream().map(DMResDto::from).toList();
        }catch (Exception e){
            log.error("INTERNAL SERVER ERROR");
            throw new CoreApiException(ErrorType.DEFAULT_ERROR);
        }
    }
}
