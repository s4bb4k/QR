package com.example.exception;

import com.example.dto.ApiErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EmvParsingException.class)
    public ResponseEntity<ApiErrorResponse> handleEmvException(
            EmvParsingException ex,
            HttpServletRequest request) {

        ApiErrorResponse error = ApiErrorResponse.builder()
                .code(ex.getErrorCode().getCode())
                .message(ex.getErrorCode().getMessage())
                .detail(ex.getMessage())
                .requestId(request.getHeader("X-Request-Id"))
                .timestamp(Instant.now())
                .build();

        HttpStatus status = switch (ex.getErrorCode()) {
            case EMV_EMPTY_INPUT, EMV_INVALID_FORMAT -> HttpStatus.BAD_REQUEST;
            case EMV_MANDATORY_TAG_MISSING -> HttpStatus.UNPROCESSABLE_ENTITY;
            default -> HttpStatus.INTERNAL_SERVER_ERROR;
        };

        return ResponseEntity.status(status).body(error);
    }

}
