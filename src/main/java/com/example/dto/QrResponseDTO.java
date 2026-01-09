package com.example.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class QrResponseDTO {
    private String requestId;
    private String emvco;
}
