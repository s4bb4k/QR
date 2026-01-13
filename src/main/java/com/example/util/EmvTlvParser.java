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
}
