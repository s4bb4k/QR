package com.example.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApiErrorResponse {

    private String code;
    private String message;
    private String detail;
    private String requestId;
    private Instant timestamp;
}
