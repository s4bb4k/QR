package com.example.util;

import org.springframework.http.HttpStatus;

public enum ErrorCatalog {

    /* ========= 400 ========= */
    INVALID_CONTENT(
            HttpStatus.BAD_REQUEST,
            "1013",
            "El contenido enviado no es válido o está mal cifrado"
    ),
    QR_EXPIRED_UPDATE(
            HttpStatus.BAD_REQUEST,
            "1013",
            "No se puede actualizar el estado de un QR expirado"
    ),
    QR_PAID_UPDATE(
            HttpStatus.BAD_REQUEST,
            "1013",
            "No se puede actualizar el estado de un QR pagado"
    ),
    QR_CANCELLED_UPDATE(
            HttpStatus.BAD_REQUEST,
            "1013",
            "No se puede actualizar el estado de un QR cancelado"
    ),
    QR_DISABLED_UPDATE(
            HttpStatus.BAD_REQUEST,
            "1013",
            "No se puede actualizar el estado de un QR inhabilitado"
    ),
    QR_STATIC_UPDATE(
            HttpStatus.BAD_REQUEST,
            "1013",
            "No se puede actualizar el estado de un QR estático, solo se puede inhabilitar"
    ),
    QR_DYNAMIC_UPDATE(
            HttpStatus.BAD_REQUEST,
            "1013",
            "No se puede actualizar el estado de un QR dinámico, solo se puede cancelar o pagar"
    ),
    INVALID_HASH(
            HttpStatus.BAD_REQUEST,
            "1013",
            "El hash de seguridad no coincide"
    ),
    QR_EXPIRED(
            HttpStatus.BAD_REQUEST,
            "1013",
            "El QR se encuentra expirado"
    ),
    QR_PAID(
            HttpStatus.BAD_REQUEST,
            "1013",
            "El QR se encuentra pagado"
    ),
    QR_DISABLED(
            HttpStatus.BAD_REQUEST,
            "1013",
            "El QR se encuentra inhabilitado"
    ),
    QR_CANCELLED(
            HttpStatus.BAD_REQUEST,
            "1013",
            "El QR se encuentra cancelado"
    ),

    /* ========= 401 ========= */
    UNAUTHORIZED(
            HttpStatus.UNAUTHORIZED,
            "1013",
            "Token inválido o expirado"
    ),

    /* ========= 403 ========= */
    FORBIDDEN(
            HttpStatus.FORBIDDEN,
            "1013",
            "El cliente no tiene permisos para acceder a este recurso"
    ),

    /* ========= 5xx ========= */
    INTERNAL_ERROR(
            HttpStatus.INTERNAL_SERVER_ERROR,
            "1005",
            "Error inesperado en el servidor"
    ),
    BAD_GATEWAY(
            HttpStatus.BAD_GATEWAY,
            "1005",
            "Problema de comunicación entre servidores. Intente nuevamente más tarde"
    ),
    SERVICE_UNAVAILABLE(
            HttpStatus.SERVICE_UNAVAILABLE,
            "1005",
            "El servicio no está disponible temporalmente. Intente nuevamente más tarde"
    );

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    ErrorCatalog(HttpStatus httpStatus, String code, String message) {
        this.httpStatus = httpStatus;
        this.code = code;
        this.message = message;
    }

    public HttpStatus httpStatus() {
        return httpStatus;
    }

    public String code() {
        return code;
    }

    public String message() {
        return message;
    }
}

