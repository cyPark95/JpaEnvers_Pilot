package com.example.securitypilot.common.security.token;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Token {

    private final String grantType;
    private final String accessToken;
    private Long accessTokenExpiresIn;
    private final String refreshToken;
    private Long refreshTokenExpiresIn;
}
