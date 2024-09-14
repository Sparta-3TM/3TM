package com.sparta3tm.authserver.controller;

import com.sparta3tm.authserver.application.DMService;
import com.sparta3tm.authserver.application.dtos.DM.DMCreateReqDto;
import com.sparta3tm.authserver.application.dtos.DM.DMUpdateReqDto;
import com.sparta3tm.common.support.error.ErrorType;
import com.sparta3tm.common.support.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/delivery_managers")
@RequiredArgsConstructor
public class DMController {

    private final DMService dmService;

    @PostMapping
    public ApiResponse<?> createDM(
            @RequestHeader(name = "X-USER-ROLE", required = false) String userRole,
            @RequestBody DMCreateReqDto dmCreateReqDto) {
        if("MASTER".equals(userRole) || "HUBADMIN".equals(userRole) || "SHIPPER".equals(userRole)) {
            return ApiResponse.success(dmService.createDM(dmCreateReqDto));
        }else{
            return ApiResponse.error(ErrorType.INVALID);
        }
    }

    @PutMapping("/{delivery_managers_id}")
    public ApiResponse<?> updateDM(
            @RequestHeader(name = "X-USER-ROLE", required = false) String userRole,
            @PathVariable(name = "delivery_managers_id") Long deliveryManagersId,
            @RequestBody DMUpdateReqDto dmUpdateReqDto
    ){
        if("MASTER".equals(userRole) || "HUBADMIN".equals(userRole) || "SHIPPER".equals(userRole)) {
            return ApiResponse.success(dmService.updateDM(deliveryManagersId, dmUpdateReqDto));
        }else{
            return ApiResponse.error(ErrorType.INVALID);
        }

    }

    @DeleteMapping("/{delivery_managers_id}")
    public ApiResponse<?> deleteDM(
            @RequestHeader(name = "X-USER-ROLE", required = false) String userRole,
            @RequestHeader(name = "X-USER-ID", required = false) String userId,
            @PathVariable(name = "delivery_managers_id") Long deliveryManagersId
    ){
        if("MASTER".equals(userRole) || "HUBADMIN".equals(userRole) || "SHIPPER".equals(userRole)) {
            return ApiResponse.success(dmService.deleteDM(deliveryManagersId, userId));
        }else{
            return ApiResponse.error(ErrorType.INVALID);
        }
    }

    @GetMapping("/{delivery_managers_id}")
    public ApiResponse<?> getDMById(
            @RequestHeader(name = "X-USER-ROLE", required = false) String userRole,
            @PathVariable(name = "delivery_managers_id") Long deliveryManagersId){
        if("MASTER".equals(userRole) || "HUBADMIN".equals(userRole) || "SHIPPER".equals(userRole)) {
            return ApiResponse.success(dmService.getDeliveryManager(deliveryManagersId));
        }else{
            return ApiResponse.error(ErrorType.INVALID);
        }
    }

    @GetMapping
    public ApiResponse<?> searchProduct(
            @RequestHeader(name = "X-USER-ROLE", required = false) String userRole,
            @RequestParam(defaultValue = "1", name = "page") int page,
            @RequestParam(defaultValue = "10", name = "size") int size,
            @RequestParam(defaultValue = "createdAt", name = "sort") String sort,
            @RequestParam(defaultValue = "DESC", name = "direction") String direction,
            @RequestParam(required = false, name = "keyword") String keyword
    ){
        Pageable pageable = PageRequest.of(page-1, size, Sort.by(Sort.Direction.fromString(direction), sort));
        if("MASTER".equals(userRole) || "HUBADMIN".equals(userRole) || "SHIPPER".equals(userRole)) {
            return ApiResponse.success(dmService.searchDM(keyword, pageable));
        }else{
            return ApiResponse.error(ErrorType.INVALID);
        }

    }

}
