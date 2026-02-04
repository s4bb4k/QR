package com.example.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
public class MerchantAccount2DTO {
    private String globallyUniqueIdentifier;
    private String aliasType;
    private String aliasValue;
}
