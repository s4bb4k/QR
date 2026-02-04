package com.example.dto;

import jakarta.validation.constraints.NotBlank;

public record EmvQrRequest(
        String qr
) {
}
