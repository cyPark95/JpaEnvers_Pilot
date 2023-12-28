package com.example.securitypilot.common.security.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SecurityErrorCode {

    TOKEN_EXPIRED("0001", "만료된 토큰입니다."),
    ;

    public static final String SECURITY_ERROR_CODE_KEY = "errorCode";

    private final String code;
    private final String message;
}
