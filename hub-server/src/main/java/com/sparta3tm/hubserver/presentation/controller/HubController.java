package com.sparta3tm.hubserver.presentation.controller;

import com.sparta3tm.common.support.response.ApiResponse;
import com.sparta3tm.hubserver.application.dto.hub.RequestHubDto;
import com.sparta3tm.hubserver.application.dto.hub.ResponseHubDto;
import com.sparta3tm.hubserver.application.dto.hub.ResponsePageHubDto;
import com.sparta3tm.hubserver.application.service.HubService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/hubs")
@RequiredArgsConstructor
@Slf4j
public class HubController {

    private final String USER_ID = "X-USER-ID";
    private final String USER_ROLE = "X-USER-ROLE";
    private final HubService hubService;

    @PostMapping
    public ApiResponse<?> createHub(@RequestBody RequestHubDto requestHubDto,
                                    @RequestHeader(name = USER_ID, required = false) String userId) {
        log.info("왜 안와요 씨발련아");

        ResponseHubDto data = hubService.createHub(requestHubDto);
        return ApiResponse.success(data);
    }

    @GetMapping("/{hubId}")
    public ApiResponse<?> searchHubById(@PathVariable Long hubId,
                                        @RequestHeader(name = USER_ID, required = false) String userId) {
        ResponseHubDto data = hubService.searchHubById(hubId);
        return ApiResponse.success(data);
    }

    @GetMapping
    public ApiResponse<?> searchHubList(@RequestParam(defaultValue = "0") int page,
                                        @RequestParam(defaultValue = "10") int size,
                                        @RequestParam(defaultValue = "createdAt") String sort,
                                        @RequestParam(defaultValue = "DESC") String direction,
                                        @RequestHeader(name = USER_ID, required = false) String userId) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(direction), sort));
        ResponsePageHubDto data = hubService.searchHubList(pageable);
        return ApiResponse.success(data);
    }

    @PutMapping("/{hubId}")
    public ApiResponse<?> updateHub(@PathVariable Long hubId, @RequestBody RequestHubDto requestHubDto,
                                    @RequestHeader(name = USER_ID, required = false) String userId) {
        ResponseHubDto data = hubService.updateHub(hubId, requestHubDto);
        return ApiResponse.success(data);
    }

    @DeleteMapping("/{hubId}")
    public ApiResponse<?> deleteHub(@PathVariable Long hubId, @RequestHeader(USER_ID) String userId) {
        hubService.deleteHub(hubId, userId);
        return ApiResponse.success();
    }

    @PatchMapping("/{hubId}/update_manager")
    public ApiResponse<?> updateManager(@PathVariable Long hubId,
                                        @RequestHeader(USER_ID) String userId,
                                        @RequestHeader(USER_ROLE) String userRole) {
        ResponseHubDto data = hubService.updateManager(hubId, userId, userRole);
        return ApiResponse.success(data);
    }

}
