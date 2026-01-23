package com.example.exception;

import com.example.util.ErrorCatalog;

public class ApiException extends RuntimeException {

    private final ErrorCatalog error;

    public ApiException(ErrorCatalog error) {
        super(error.message());
        this.error = error;
    }

    public ErrorCatalog error() {
        return error;
    }

}
