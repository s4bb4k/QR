package com.example.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QrRequestDTO {
    @NotNull
    private MetaDTO meta;

    @NotNull
    private DataDTO data;
}
