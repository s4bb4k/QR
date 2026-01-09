package com.example.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MetaDTO {

    @NotBlank
    private String requestId;
    @NotBlank
    private String version;
    @NotBlank
    private String timestamp;

}
