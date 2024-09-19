package com.sparta3tm.hubserver.presentation.controller;

import com.sparta3tm.common.support.response.ApiResponse;
import com.sparta3tm.hubserver.application.dto.hmi.request.AddUpdateHMIDto;
import com.sparta3tm.hubserver.application.dto.hmi.request.RemoveUpdateHMIDto;
import com.sparta3tm.hubserver.application.dto.hmi.request.RequestHMIDto;
import com.sparta3tm.hubserver.application.dto.hmi.response.ResponseHMIDto;
import com.sparta3tm.hubserver.application.dto.hub.response.ResponseHubManagerDto;
import com.sparta3tm.hubserver.application.service.HMIService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/hub_movement_infos")
@Tag(name = "Hub-Movement-infos", description = "Hub-Movement-infos API")
@Slf4j(topic = "HMI-Controller")
public class HMIController {

    private final HMIService hmiService;
    private final String USER_ID = "X-USER-ID";
    private final String USER_ROLE = "X-USER-ROLE";

    @Operation(summary = "HMI Create")
    @PostMapping
    public ApiResponse<?> createHmi(@RequestBody RequestHMIDto requestHMIDto,
                                    @RequestHeader(name = USER_ID) String userId,
                                    @RequestHeader(name = USER_ROLE) String userRole) {
        ResponseHMIDto data = hmiService.createHmi(requestHMIDto);
        return ApiResponse.success(data);
    }

    @Operation(summary = "HMI Get One By Id")
    @GetMapping("/{hmiId}")
    public ApiResponse<?> searchHmiById(@PathVariable Long hmiId,
                                        @RequestHeader(name = USER_ID) String userId,
                                        @RequestHeader(name = USER_ROLE) String userRole) {
        ResponseHMIDto data = hmiService.searchHmiById(hmiId);
        return ApiResponse.success(data);
    }

    @Operation(summary = "HMI Add Update")
    @PutMapping("/add/{hmiId}")
    public ApiResponse<?> addUpdateHmi(@PathVariable Long hmiId, @RequestBody AddUpdateHMIDto addUpdateHMIDto,
                                       @RequestHeader(name = USER_ID) String userId,
                                       @RequestHeader(name = USER_ROLE) String userRole) {
        ResponseHMIDto data = hmiService.addUpdateHmi(hmiId, addUpdateHMIDto, userId, userRole);
        return ApiResponse.success();
    }

    @Operation(summary = "HMI Remove Update")
    @PutMapping("/remove/{hmiId}")
    public ApiResponse<?> removeUpdateHmi(@PathVariable Long hmiId, @RequestBody RemoveUpdateHMIDto removeUpdateHMIDto,
                                          @RequestHeader(name = USER_ID) String userId,
                                          @RequestHeader(name = USER_ROLE) String userRole) {
        ResponseHMIDto data = hmiService.removeUpdateHmi(hmiId, removeUpdateHMIDto, userId, userRole);
        return ApiResponse.success();
    }

    @Operation(summary = "HMI Delete")
    @DeleteMapping("/{hmiId}")
    public ApiResponse<?> deleteHmi(@PathVariable Long hmiId,
                                    @RequestHeader(name = USER_ID) String userId,
                                    @RequestHeader(name = USER_ROLE) String userRole) {
        hmiService.deleteHmi(hmiId, userId, userRole);
        return ApiResponse.success();
    }

    @Operation(summary = "HMI Manager Search")
    @GetMapping("/{hmiId}/manager")
    public ApiResponse<?> searchHmiManager(@PathVariable Long hmiId,
                                        @RequestHeader(name = USER_ID) String userId,
                                        @RequestHeader(name = USER_ROLE) String userRole) {
        List<ResponseHubManagerDto> data = hmiService.searchHmiManager(hmiId, userId, userRole);
        return ApiResponse.success(data);
    }

}
