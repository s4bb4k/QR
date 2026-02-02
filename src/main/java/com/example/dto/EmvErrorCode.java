package com.example.dto;

public enum EmvErrorCode {

    EMV_EMPTY_INPUT("EMV_001", "El código EMVCo está vacío"),
    EMV_INVALID_FORMAT("EMV_002", "Formato EMVCo inválido"),
    EMV_MANDATORY_TAG_MISSING("EMV_003", "Faltan tags obligatorios"),
    EMV_PARSE_ERROR("EMV_004", "Error al parsear el código EMVCo"),
    INTERNAL_ERROR("GEN_001", "Error interno");

    private final String code;
    private final String message;

    EmvErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() { return code; }
    public String getMessage() { return message; }

}
