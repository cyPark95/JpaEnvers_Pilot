package com.example.securitypilot.domain.auth.token;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Token {

    private final String accessToken;
    private final String grantType;
    private Long expiresIn;
    private final String refreshToken;
}
