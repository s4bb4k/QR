package com.example.util;

import java.nio.charset.StandardCharsets;

public class Crc16Util {

    public static String calculate(String val) {

        if (val == null || val.isBlank()) {
            throw new IllegalArgumentException("CRC no puede calcularse sobre null/vacío");
        }

        int crc = 0xFFFF;

        for (char c : val.toCharArray()) {
            crc ^= (c << 8);
            for (int i = 0; i < 8; i++) {
                crc = ((crc & 0x8000) != 0)
                        ? (crc << 1) ^ 0x1021
                        : (crc << 1);
            }
        }
        return String.format("%04X", crc & 0xFFFF);
    }

}

