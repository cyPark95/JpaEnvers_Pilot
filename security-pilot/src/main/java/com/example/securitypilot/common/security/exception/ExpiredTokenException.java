package com.example.securitypilot.common.security.exception;

public class ExpiredTokenException extends DefaultAuthenticationException {

    private final static String TOKEN_EXPIRED_CODE = "0001";

    public ExpiredTokenException(String msg, Throwable cause) {
        super(TOKEN_EXPIRED_CODE, msg, cause);
    }
}
