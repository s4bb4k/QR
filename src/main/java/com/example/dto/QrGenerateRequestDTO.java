package com.example.dto;

public record QrGenerateRequestDTO(
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
            AmountInformation amountInformation,
            Target target,
            PurposeInformation purposeInformation,
            UseCaseInformation useCaseInformation
    ) {}

    public record AmountInformation(
            String amount,
            String currency
    ) {}

    public record Target(
            String personType,
            String fullName,
            String businessName,
            Location location,
            Alias alias
    ) {}

    public record Location(
            String city,
            String postalCode
    ) {}

    public record Alias(
            String aliasType,
            String aliasValue
    ) {}

    public record PurposeInformation(
            String transactionPurpose
    ) {}

    public record UseCaseInformation(
            String qrType,
            String categoryCode,
            String terminal,
            String vat,
            String vatBase,
            String tax,
            String channel
    ) {}
}
