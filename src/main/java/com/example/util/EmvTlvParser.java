package com.example.util;

import com.example.dto.EmvPayload;

import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;

public class EmvTlvParser {

    private EmvTlvParser() {}

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

    public static Map<String, String> parseTLV2(String data) {
        if (data == null || data.isBlank()) {
            return Map.of();
        }

        Map<String, String> map = new LinkedHashMap<>();
        int index = 0;
        int n = data.length();

        while (index + 4 <= n) {
            String tag = data.substring(index, index + 2);

            char l1 = data.charAt(index + 2);
            char l2 = data.charAt(index + 3);

            if (!Character.isDigit(l1) || !Character.isDigit(l2)) {
                throw new IllegalArgumentException("Formato TLV inválido en tag " + tag);
            }

            int length = Integer.parseInt(data.substring(index + 2, index + 4));
            int valueStart = index + 4;
            int valueEnd = valueStart + length;

            if (valueEnd > n) {
                throw new IllegalArgumentException("Longitud TLV inconsistente en tag " + tag);
            }

            String value = data.substring(valueStart, valueEnd);
            map.put(tag, value);

            index = valueEnd;
        }

        return map;
    }

    public static EmvPayload parse3(String payload) {

        if (payload == null || payload.length() < 4) {
            throw new IllegalArgumentException("Payload vacío o incompleto");
        }

        /* ===================== 1️⃣ CRC ===================== */

        CrcInfo crcInfo = findCrc(payload);
        boolean crcValid = false;
        String crcReported = null;

        if (crcInfo != null) {
            crcReported = crcInfo.valueHex;

            int calculatedCrc = crc16CcittFalse(
                    payload.substring(0, crcInfo.index + 4)
                            .getBytes(StandardCharsets.US_ASCII)
            );

            crcValid = crcReported.equalsIgnoreCase(
                    String.format("%04X", calculatedCrc)
            );
        }

        /* ===================== 2️⃣ PARSE TLV ===================== */

        ParseCursor cursor = new ParseCursor(payload);
        Map<String, Object> root = parseLevel(cursor);

        /* ===================== 3️⃣ FLATTEN ===================== */

        Map<String, String> flat = new LinkedHashMap<>();
        flatten("", root, flat);

        /* ===================== 4️⃣ MULTILLAVE BRE-B ===================== */

        String multillaveBreB = flat.entrySet().stream()
                .filter(e -> e.getKey().startsWith("26.00"))
                .map(Map.Entry::getValue)
                .findFirst()
                .orElse(null);

        if (multillaveBreB == null && crcValid) {

            String merchant = flat.get("50.01");

            if (merchant != null && merchant.length() > 8) {
                merchant = merchant.substring(merchant.length() - 8);
            }

            if (merchant != null) {
                multillaveBreB = String.format("%10s", merchant)
                        .replace(' ', '0');
            }
        }

        /* ===================== RESULTADO ===================== */

        return new EmvPayload(
                root,
                flat,
                crcValid,
                crcReported,
                multillaveBreB
        );

    }


    /* ===================== PARSER TLV ===================== */

    private static final class ParseCursor {
        final String s;
        int i = 0;
        ParseCursor(String s) { this.s = s; }
    }

    private static Map<String, Object> parseLevel(ParseCursor c) {
        Map<String, Object> out = new LinkedHashMap<>();
        while (c.i + 4 <= c.s.length()) {
            String tag = c.s.substring(c.i, c.i + 2);
            int len = Integer.parseInt(c.s.substring(c.i + 2, c.i + 4));
            int vStart = c.i + 4;
            int vEnd = vStart + len;

            if (vEnd > c.s.length()) {
                throw new IllegalArgumentException("TLV incompleto en tag " + tag);
            }

            String value = c.s.substring(vStart, vEnd);
            c.i = vEnd;

            if (isTemplate(tag)) {
                out.put(tag, parseLevel(new ParseCursor(value)));
            } else {
                out.put(tag, value);
            }
        }
        return out;
    }

    private static boolean isTemplate(String tag) {
        int t = Integer.parseInt(tag);
        return (26 <= t && t <= 51) || t == 62 || t == 64 || (80 <= t && t <= 99);
    }

    /* ===================== FLATTEN ===================== */

    @SuppressWarnings("unchecked")
    private static void flatten(String prefix, Map<String, Object> node, Map<String, String> out) {
        for (var e : node.entrySet()) {
            String key = prefix.isEmpty() ? e.getKey() : prefix + "." + e.getKey();
            if (e.getValue() instanceof Map) {
                flatten(key, (Map<String, Object>) e.getValue(), out);
            } else {
                out.put(key, e.getValue().toString());
            }
        }
    }


    /* ===================== CRC ===================== */

    private static final class CrcInfo {
        final int index;
        final String valueHex;
        CrcInfo(int index, String valueHex) {
            this.index = index;
            this.valueHex = valueHex;
        }
    }

    private static CrcInfo findCrc(String payload) {
        int i = 0;
        while (i + 4 <= payload.length()) {
            String tag = payload.substring(i, i + 2);
            int len = Integer.parseInt(payload.substring(i + 2, i + 4));
            if ("63".equals(tag)) {
                return new CrcInfo(i, payload.substring(i + 4, i + 8));
            }
            i += 4 + len;
        }
        return null;
    }

    public static int crc16CcittFalse(byte[] data) {
        int crc = 0xFFFF;
        for (byte b : data) {
            crc ^= (b & 0xFF) << 8;
            for (int i = 0; i < 8; i++) {
                crc = ((crc & 0x8000) != 0)
                        ? (crc << 1) ^ 0x1021
                        : (crc << 1);
            }
        }
        return crc & 0xFFFF;
    }

}
