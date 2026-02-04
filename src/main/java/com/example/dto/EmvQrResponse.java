package com.example.dto;

import java.util.Map;

public record EmvQrResponse(
        boolean crcValid,
        String crcValue,
        String multillaveBreB,
        Map<String, String> flat
) {
}
