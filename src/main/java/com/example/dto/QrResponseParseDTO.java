package com.example.dto;

import java.util.Map;

public record QrResponseParseDTO(
        Meta meta,
        Map<String, Object> data
) {

    public record Meta(
            String requestId,
            String timestamp,
            String status,
            String statusCode,
            String statusDesc
    ) {}
}
