package com.example.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DataDTO {
    @NotBlank
    private String movementType;

    @NotNull
    private BigDecimal amount;

    @NotBlank
    private String currency;

    @NotBlank
    private String pointOfInitiationMethod;

    @Valid
    private MerchantAccountDTO merchantAccountInformation;

    @Valid
    private AdditionalMerchantDTO additionalMerchantInformation;

    @Valid
    private CustomDataDTO customData;
}
