package com.sparta3tm.hubserver.presentation;

import com.sparta3tm.common.support.response.ApiResponse;
import com.sparta3tm.hubserver.application.dto.RequestHubDto;
import com.sparta3tm.hubserver.application.dto.ResponseHubDto;
import com.sparta3tm.hubserver.application.dto.ResponsePageHubDto;
import com.sparta3tm.hubserver.application.service.HubService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/hubs")
@RequiredArgsConstructor
public class HubController {

    private final String USER_NAME = "X-USER-NAME";
    private final HubService hubService;

    @PostMapping
    public ApiResponse<?> createHub(@RequestBody RequestHubDto requestHubDto) {
        ResponseHubDto data = hubService.createHub(requestHubDto);
        return ApiResponse.success(data);
    }

    @GetMapping("/{hubId}")
    public ApiResponse<?> searchHubById(@PathVariable Long hubId) {
        ResponseHubDto data = hubService.searchHubById(hubId);
        return ApiResponse.success(data);
    }

    @GetMapping
    public ApiResponse<?> searchHubList(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sort,
            @RequestParam(defaultValue = "DESC") String direction) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(direction), sort));
        ResponsePageHubDto data = hubService.searchHubList(pageable);
        return ApiResponse.success(data);
    }

    @PutMapping("/{hubId}")
    public ApiResponse<?> updateHub(@PathVariable Long hubId, @RequestBody RequestHubDto requestHubDto) {
        ResponseHubDto data = hubService.updateHub(hubId, requestHubDto);
        return ApiResponse.success(data);
    }

    @DeleteMapping("/{hubId}")
    public ApiResponse<?> deleteHub(@PathVariable Long hubId, @RequestHeader(USER_NAME) String username) {
        ResponseHubDto data = hubService.deleteHub(hubId, username);
        return ApiResponse.success();
    }

}
