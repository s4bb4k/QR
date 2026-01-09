package com.example.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MerchantAccountDTO {
    private String aliasType;
    private String aliasValue;
    private String merchantCode;
    private String merchantName;
    private String merchantCity;
    private String postalCode;
    private String merchantCategoryCode;
}
