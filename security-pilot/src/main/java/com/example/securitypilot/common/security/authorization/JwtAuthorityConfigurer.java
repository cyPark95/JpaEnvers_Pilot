package com.example.securitypilot.common.security.authorization;

import com.example.securitypilot.common.security.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;

@RequiredArgsConstructor
public class JwtAuthorityConfigurer
        extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    private final JwtProvider jwtProvider;
    private final UserDetailsService userDetailsService;

    @Override
    public void init(HttpSecurity builder) {
    }

    @Override
    public void configure(HttpSecurity builder) {
        builder.addFilterBefore(
                new JwtAuthorizationFilter(jwtProvider, userDetailsService),
                AuthorizationFilter.class
        );
    }
}
