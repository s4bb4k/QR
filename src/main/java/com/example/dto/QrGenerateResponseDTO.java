package com.example.dto;

public record QrGenerateResponseDTO(
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
            String creationDate,
            String qrStatus,
            String movementType,
            String duration,
            String expirationDate,
            String imageB64
    ) {}

    public record Error(
            String code,
            String message
    ) {}
}
