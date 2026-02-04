package com.example.dto;

import java.util.Iterator;
import java.util.Map;

public class EmvPayload {

    private final Map<String, Object> tree;
    private final Map<String, String> flat;
    private final boolean crcValid;
    private final String crcValueHex;
    private final String multillaveBreB;

    public EmvPayload(
            Map<String, Object> tree,
            Map<String, String> flat,
            boolean crcValid,
            String crcValueHex,
            String multillaveBreB
    ) {
        this.tree = tree;
        this.flat = flat;
        this.crcValid = crcValid;
        this.crcValueHex = crcValueHex;
        this.multillaveBreB = multillaveBreB;
    }

    public Map<String, Object> getTree() { return tree; }
    public Map<String, String> getFlat() { return flat; }
    public boolean isCrcValid() { return crcValid; }
    public String getCrcValueHex() { return crcValueHex; }
    public String getMultillaveBreB() { return multillaveBreB; }

    /* ======== SALIDA JSON ======== */
    public String toJson() {
        StringBuilder sb = new StringBuilder();
        sb.append("{\n");
        sb.append("  \"crcValid\": ").append(crcValid).append(",\n");
        sb.append("  \"crcValue\": \"").append(crcValueHex).append("\",\n");
        sb.append("  \"multillaveBreB\": \"").append(multillaveBreB).append("\",\n");
        sb.append("  \"flat\": ").append(mapToJson(flat)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    /* ===================== JSON UTIL ===================== */

    private static String mapToJson(Map<String, String> map) {
        StringBuilder sb = new StringBuilder("{\n");
        Iterator<Map.Entry<String, String>> it = map.entrySet().iterator();
        while (it.hasNext()) {
            var e = it.next();
            sb.append("    \"").append(e.getKey()).append("\": \"")
                    .append(e.getValue().replace("\"", "\\\"")).append("\"");
            if (it.hasNext()) sb.append(",");
            sb.append("\n");
        }
        sb.append("  }");
        return sb.toString();
    }
}
