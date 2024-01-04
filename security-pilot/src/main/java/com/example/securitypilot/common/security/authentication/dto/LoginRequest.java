package com.example.securitypilot.common.security.authentication.dto;

import org.springframework.util.StringUtils;

public record LoginRequest(String email, String password) {

    public LoginRequest {
        if (!StringUtils.hasText(email)) {
            throw new RuntimeException("이메일은 필수 값 입니다.");
        }

        if (!StringUtils.hasText(password)) {
            throw new RuntimeException("비밀번호는 필수 값 입니다.");
        }
    }
}
