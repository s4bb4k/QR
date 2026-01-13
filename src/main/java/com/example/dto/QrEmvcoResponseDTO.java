package com.example.dto;

import lombok.Data;

@Data
public class QrEmvcoResponseDTO {
    private String payloadFormatIndicator;
    private String pointOfInitiationMethod;
    private String merchantCategoryCode;
    private String transactionCurrency;
    private String transactionAmount;
    private String countryCode;
    private String merchantName;
    private String merchantCity;
    private MerchantAccount2DTO merchantAccount;
}
