package com.example.exception;

import com.example.dto.EmvErrorCode;

public class EmvParsingException extends RuntimeException {

    private final EmvErrorCode errorCode;

    public EmvParsingException(EmvErrorCode errorCode, String detail) {
        super(detail);
        this.errorCode = errorCode;
    }

    public EmvErrorCode getErrorCode() {
        return errorCode;
    }

}
