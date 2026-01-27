package com.example.dto;

public record QrQueryResponseDTO(
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
            String qrCode,
            String creationDateTime,
            String qrStatus,
            Integer duration,
            String expirationDateTime,
            String imageB64
    ) {}

    public record Error(
            String code,
            String message
    ) {}
}
