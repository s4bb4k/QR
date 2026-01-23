package com.example.dto;

public record QrRequestParseDto(
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
            String qrCode
    ) {}
}


