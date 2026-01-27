package com.example.dto;

import java.util.Map;

public record QrValidateResponseDTO(
        Meta meta,
        Map<String, Object> data,
        Error error
) {
    public record Meta(
            String requestId,
            String timestamp,
            String status,
            String statusCode,
            String statusDesc
    ) {}

    public record Error(
            String code,
            String message
    ) {}
}
