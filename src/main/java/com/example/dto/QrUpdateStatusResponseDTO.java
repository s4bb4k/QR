package com.example.dto;

public record QrUpdateStatusResponseDTO(
        Meta meta,
        Data data,
        Error error
) {
    public record Meta(
            String requestId,
            String timestamp,
            String status,
            String statusCode,
            String statusDesc
    ) {}

    public record Data(
            String id,
            String qrStatus,
            String lastModifiedDateTime
    ) {}

    public record Error(
            String code,
            String message
    ) {}
}
