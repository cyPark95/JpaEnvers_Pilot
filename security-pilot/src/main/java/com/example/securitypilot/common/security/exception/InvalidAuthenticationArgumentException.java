package com.example.securitypilot.common.security.exception;

import org.springframework.security.core.AuthenticationException;

public class InvalidAuthenticationArgumentException extends AuthenticationException {

    public InvalidAuthenticationArgumentException(String msg) {
        super(msg);
    }
}
