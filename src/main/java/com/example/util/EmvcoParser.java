package com.example.util;

import java.util.HashMap;
import java.util.Map;

public class EmvcoParser {

    public static Map<String, String> parse(String emv) {

        Map<String, String> result = new HashMap<>();
        int index = 0;

        while (index + 4 <= emv.length()) {

            String tag = emv.substring(index, index + 2);
            String lenStr = emv.substring(index + 2, index + 4);

            if (!lenStr.matches("\\d{2}")) break;

            int length = Integer.parseInt(lenStr);
            int valueStart = index + 4;
            int valueEnd = valueStart + length;

            if (valueEnd > emv.length()) break;

            if ("63".equals(tag)) break; // CRC → STOP

            String value = emv.substring(valueStart, valueEnd);

            if (Integer.parseInt(tag) >= 26 && Integer.parseInt(tag) <= 51) {
                parseSubTags(tag, value, result);
            } else {
                result.put(tag, value);
            }

            index = valueEnd;
        }
        return result;
    }

    private static void parseSubTags(String parent, String value, Map<String, String> result) {

        int i = 0;
        while (i + 4 <= value.length()) {

            String tag = value.substring(i, i + 2);
            String lenStr = value.substring(i + 2, i + 4);

            if (!lenStr.matches("\\d{2}")) break;

            int length = Integer.parseInt(lenStr);
            int valueStart = i + 4;
            int valueEnd = valueStart + length;

            if (valueEnd > value.length()) break;

            result.put(parent + "." + tag, value.substring(valueStart, valueEnd));
            i = valueEnd;
        }
    }

}
