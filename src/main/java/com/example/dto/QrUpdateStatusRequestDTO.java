package com.example.dto;

public record QrUpdateStatusRequestDTO(
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
            String status
    ) {}
}
