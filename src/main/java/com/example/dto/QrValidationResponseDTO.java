package com.example.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QrValidationResponseDTO {
    private boolean equals;
    private QrRequestDTO parsed;
    private List<String> differences;
}
