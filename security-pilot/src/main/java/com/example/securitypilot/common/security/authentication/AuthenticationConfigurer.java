package com.example.securitypilot.common.security.authentication;

import com.example.securitypilot.common.security.jwt.JwtProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.SecurityConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
public class AuthenticationConfigurer
        implements SecurityConfigurer<DefaultSecurityFilterChain, HttpSecurity> {

    private final ObjectMapper objectMapper;
    private final JwtProvider jwtProvider;

    @Override
    public void init(HttpSecurity builder) {
    }

    @Override
    public void configure(HttpSecurity builder) {
        builder.addFilterBefore(
                getSecurityAuthenticationFilter(builder),
                UsernamePasswordAuthenticationFilter.class
        );
    }

    private SecurityAuthenticationFilter getSecurityAuthenticationFilter(HttpSecurity builder) {
        SecurityAuthenticationFilter authenticationFilter =
                new SecurityAuthenticationFilter(
                        builder.getSharedObject(AuthenticationManager.class),
                        objectMapper
                );
        authenticationFilter.setAuthenticationSuccessHandler(getAuthenticationSuccessHandler());
        authenticationFilter.setAuthenticationFailureHandler(getAuthenticationFailureHandler());
        return authenticationFilter;
    }

    private SecurityAuthenticationSuccessHandler getAuthenticationSuccessHandler() {
        return new SecurityAuthenticationSuccessHandler(
                objectMapper,
                jwtProvider
        );
    }

    private static SecurityAuthenticationFailureHandler getAuthenticationFailureHandler() {
        return new SecurityAuthenticationFailureHandler();
    }
}
