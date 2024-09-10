package com.sparta3tm.common.support.error;

import lombok.Getter;
import org.springframework.boot.logging.LogLevel;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorType {

    DEFAULT_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, ErrorCode.E500, "An unexpected error has occurred.", LogLevel.ERROR),

    NOT_FOUND_ERROR(HttpStatus.NOT_FOUND, ErrorCode.E404, "Not found data.", LogLevel.ERROR),
    BAD_REQUEST(HttpStatus.BAD_REQUEST, ErrorCode.E400, "Bad Request.", LogLevel.ERROR);


    private final HttpStatus status;

    private final ErrorCode code;

    private final String message;

    private final LogLevel logLevel;

    ErrorType(HttpStatus status, ErrorCode code, String message, LogLevel logLevel) {

        this.status = status;
        this.code = code;
        this.message = message;
        this.logLevel = logLevel;
    }

}
