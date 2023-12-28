package com.example.securitypilot.common.response;

public record ErrorResponse(
        String code,
        String message
) {

    public static ErrorResponse of(String message) {
        return new ErrorResponse("-1", message);
    }

    public static ErrorResponse of(String code, String message) {
        return new ErrorResponse(code, message);
    }
}
