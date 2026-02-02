package com.example.util;

import java.util.LinkedHashMap;
import java.util.Map;

public class EmvTlvParser {
    public static Map<String, String> parse(String data) {

        Map<String, String> map = new LinkedHashMap<>();
        int index = 0;

        while (index + 4 <= data.length()) {
            String tag = data.substring(index, index + 2);
            String lengthStr = data.substring(index + 2, index + 4);
            int length = Integer.parseInt(lengthStr);

            int valueStart = index + 4;
            int valueEnd = valueStart + length;

            if (valueEnd > data.length()) break;

            String value = data.substring(valueStart, valueEnd);
            map.put(tag, value);

            index = valueEnd;
        }
        return map;
    }

    public static Map<String, String> parseTLV(String data) {
        if (data == null) return Map.of();

        data = data.trim();
        Map<String, String> map = new LinkedHashMap<>();
        int index = 0;
        int n = data.length();

        while (index + 4 <= n) { // Necesitamos al menos TT(2) + LL(2)
            String tag = data.substring(index, index + 2);

            // Validación de dígitos para LL
            char l1 = data.charAt(index + 2);
            char l2 = data.charAt(index + 3);
            if (!Character.isDigit(l1) || !Character.isDigit(l2)) {
                // Formato inválido: rompemos o lanzamos excepción
                break;
            }

            int length = Integer.parseInt(data.substring(index + 2, index + 4));
            int valueStart = index + 4;
            int valueEnd = valueStart + length;

            if (valueEnd > n) {
                // Longitud inconsistente con el tamaño restante
                break;
            }

            String value = data.substring(valueStart, valueEnd);
            map.put(tag, value);

            index = valueEnd; // avanzar al siguiente TLV hermano
        }

        return map;
    }

}
