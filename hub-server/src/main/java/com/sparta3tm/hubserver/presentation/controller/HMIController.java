package com.sparta3tm.hubserver.presentation.controller;

import com.sparta3tm.common.support.response.ApiResponse;
import com.sparta3tm.hubserver.application.dto.hmi.AddUpdateHMIDto;
import com.sparta3tm.hubserver.application.dto.hmi.RemoveUpdateHMIDto;
import com.sparta3tm.hubserver.application.dto.hmi.RequestHMIDto;
import com.sparta3tm.hubserver.application.dto.hmi.ResponseHMIDto;
import com.sparta3tm.hubserver.application.dto.hub.ResponseHubManagerDto;
import com.sparta3tm.hubserver.application.service.HMIService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/hub_movement_infos")
public class HMIController {

    private final HMIService hmiService;
    private final String USER_ID = "X-USER-ID";
    private final String USER_ROLE = "X-USER-ROLE";

    @PostMapping
    public ApiResponse<?> createHmi(@RequestBody RequestHMIDto requestHMIDto,
                                    @RequestHeader(name = USER_ID) String userId,
                                    @RequestHeader(name = USER_ROLE) String userRole) {
        ResponseHMIDto data = hmiService.createHmi(requestHMIDto);
        return ApiResponse.success(data);
    }

    @GetMapping("/{hmiId}")
    public ApiResponse<?> searchHmiById(@PathVariable Long hmiId,
                                        @RequestHeader(name = USER_ID) String userId,
                                        @RequestHeader(name = USER_ROLE) String userRole) {
        ResponseHMIDto data = hmiService.searchHmiById(hmiId);
        return ApiResponse.success(data);
    }

    @PutMapping("/add/{hmiId}")
    public ApiResponse<?> addUpdateHmi(@PathVariable Long hmiId, @RequestBody AddUpdateHMIDto addUpdateHMIDto,
                                       @RequestHeader(name = USER_ID) String userId,
                                       @RequestHeader(name = USER_ROLE) String userRole) {
        ResponseHMIDto data = hmiService.addUpdateHmi(hmiId, addUpdateHMIDto, userId, userRole);
        return ApiResponse.success();
    }

    @PutMapping("/remove/{hmiId}")
    public ApiResponse<?> removeUpdateHmi(@PathVariable Long hmiId, @RequestBody RemoveUpdateHMIDto removeUpdateHMIDto,
                                          @RequestHeader(name = USER_ID) String userId,
                                          @RequestHeader(name = USER_ROLE) String userRole) {
        ResponseHMIDto data = hmiService.removeUpdateHmi(hmiId, removeUpdateHMIDto, userId, userRole);
        return ApiResponse.success();
    }

    @DeleteMapping("/{hmiId}")
    public ApiResponse<?> deleteHmi(@PathVariable Long hmiId,
                                    @RequestHeader(name = USER_ID) String userId,
                                    @RequestHeader(name = USER_ROLE) String userRole) {
        hmiService.deleteHmi(hmiId, userId, userRole);
        return ApiResponse.success();
    }

    @GetMapping("/{hmiId}/manager")
    public ApiResponse<?> searchHmiManager(@PathVariable Long hmiId,
                                        @RequestHeader(name = USER_ID) String userId,
                                        @RequestHeader(name = USER_ROLE) String userRole) {
        List<ResponseHubManagerDto> data = hmiService.searchHmiManager(hmiId, userId, userRole);
        return ApiResponse.success(data);
    }

    /**@GetMapping
     * 솔직히 페이징 조회가 이 복잡한 Hmi 에서 의미가 있나 싶음 없애는게 낫다고 판단.
    public ApiResponse<?> searchHmiList(@RequestParam(defaultValue = "0") int page,
     @RequestParam(defaultValue = "10") int size,
     @RequestParam(defaultValue = "createdAt") String sort,
     @RequestParam(defaultValue = "DESC") String direction) {
     Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(direction), sort));
     ResponsePageHMIDto data = hmiService.searchHmi(pageable);
     return ApiResponse.success(data);
     }*/

}
