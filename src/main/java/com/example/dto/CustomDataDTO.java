package com.example.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomDataDTO {
    private BigDecimal ivaValue;
    private BigDecimal ivaBase;
    private BigDecimal incValue;
    private String qrId;
    private String channel;
}
