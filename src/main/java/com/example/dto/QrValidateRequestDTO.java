package com.example.dto;

public record QrValidateRequestDTO(
        Meta meta,
        Data data
) {
    public record Meta(
            String requestId,
            String timestamp,
            String version
    ) {}

    public record Data(
            String movementType,
            UseCaseInformation useCaseInformation
    ) {}

    public record UseCaseInformation(
            String idQr,
            String crc,
            String hash
    ) {}
}
