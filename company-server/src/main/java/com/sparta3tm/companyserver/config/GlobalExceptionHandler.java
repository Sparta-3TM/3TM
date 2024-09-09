package com.sparta3tm.companyserver.config;

import com.sparta3tm.common.support.error.CoreApiException;
import com.sparta3tm.common.support.response.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j(topic = "Global Exception Handler")
@Order(Ordered.HIGHEST_PRECEDENCE)
public class GlobalExceptionHandler {

    @ExceptionHandler(CoreApiException.class)
    public ResponseEntity<ApiResponse<?>> handleGlobalException(CoreApiException e) {
        log.error("CoreApiException : {}", e.getMessage(), e);
        return ResponseEntity.status(e.getErrorType().getStatus())
                .body(ApiResponse.error(e.getErrorType(), e.getData()));
    }

}

