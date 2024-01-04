package com.example.securitypilot.common.security.exception;

import lombok.Getter;
import org.springframework.security.core.AuthenticationException;

@Getter
public class DefaultAuthenticationException extends AuthenticationException {

    private final static String DEFAULT_CODE = "-1";
    private final String code;

    public DefaultAuthenticationException(String msg, Throwable cause) {
        this(DEFAULT_CODE, msg, cause);
    }

    public DefaultAuthenticationException(String code, String msg, Throwable cause) {
        super(msg, cause);
        this.code = code;
    }
}
