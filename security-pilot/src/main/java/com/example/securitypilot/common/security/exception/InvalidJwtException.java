package com.example.securitypilot.common.security.exception;

public class InvalidJwtException extends DefaultAuthenticationException {

    public InvalidJwtException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
