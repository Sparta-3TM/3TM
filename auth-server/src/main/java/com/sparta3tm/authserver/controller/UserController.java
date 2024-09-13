package com.sparta3tm.authserver.controller;

import com.sparta3tm.authserver.application.UserService;
import com.sparta3tm.authserver.application.dtos.user.UserUpdateReqDto;
import com.sparta3tm.authserver.domain.user.User;
import com.sparta3tm.authserver.domain.user.UserRole;
import com.sparta3tm.common.support.error.ErrorType;
import com.sparta3tm.common.support.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/{userId}")
    public ApiResponse<?> getUser(
            @PathVariable Long userId) {
        return ApiResponse.success(userService.getUser(userId));
    }

    @GetMapping
    public ApiResponse<?> searchUser(
            @RequestHeader(name = "X-USER-ROLE", required = false) String userRole,
            @RequestParam(defaultValue = "1", name = "page") int page,
            @RequestParam(defaultValue = "10", name = "size") int size,
            @RequestParam(defaultValue = "createdAt", name = "sort") String sort,
            @RequestParam(defaultValue = "DESC", name = "direction") String direction,
            @RequestParam(required = false, name = "keyword") String keyword
    ) {
        if("MASTER".equals(userRole)) {
            Pageable pageable = PageRequest.of(page-1, size, Sort.by(Sort.Direction.fromString(direction), sort));
            return ApiResponse.success(userService.searchUser(keyword, pageable));
        }else{
            return ApiResponse.error(ErrorType.INVALID);
        }
    }

    @PutMapping("/{userId}")
    public ApiResponse<?> updateUser(
            @RequestHeader(name = "X-USER-ROLE", required = false) String userRole,
            @PathVariable(name = "userId") Long userId,
            @RequestBody UserUpdateReqDto userUpdateReqDto
    ){
        if("MASTER".equals(userRole)) {
            return ApiResponse.success(userService.updateUser(userId, userUpdateReqDto));
        }else{
            return ApiResponse.error(ErrorType.INVALID);
        }

    }

    @DeleteMapping("/{userId}")
    public ApiResponse<?> deleteUser(
            @RequestHeader(name = "X-USER-ROLE", required = false) String userRole,
            @RequestHeader(name = "X-USER-ID", required = false) String headUserId,
            @PathVariable(name = "userId") Long userId
    ){
        if("MASTER".equals(userRole)) {
            return ApiResponse.success(userService.deleteUser(userId, headUserId));
        }else{
            return ApiResponse.error(ErrorType.INVALID);
        }

    }
}
