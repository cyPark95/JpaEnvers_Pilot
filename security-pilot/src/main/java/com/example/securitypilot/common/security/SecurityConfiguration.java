package com.example.securitypilot.common.security;

import com.example.securitypilot.common.security.authentication.AuthenticationConfigurer;
import com.example.securitypilot.common.security.authentication.SecurityAuthenticationFailureHandler;
import com.example.securitypilot.common.security.authentication.SecurityAuthenticationFilter;
import com.example.securitypilot.common.security.authentication.SecurityAuthenticationSuccessHandler;
import com.example.securitypilot.common.security.authorization.JwtAuthorityConfigurer;
import com.example.securitypilot.common.security.exception.handler.SecurityAccessDeniedHandler;
import com.example.securitypilot.common.security.exception.handler.SecurityAuthenticationEntryPoint;
import com.example.securitypilot.common.security.jwt.JwtProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer.FrameOptionsConfig;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final ObjectMapper objectMapper;
    private final JwtProvider jwtProvider;
    private final UserDetailsService userDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .headers(header -> header.frameOptions(FrameOptionsConfig::disable).disable())
                .formLogin(AbstractHttpConfigurer::disable)
                .sessionManagement(configurer ->
                        configurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                );

        http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(PathRequest.toH2Console()).permitAll()
                        .requestMatchers("/error").permitAll()
                        .anyRequest().authenticated());

        http
                .with(
                        new AuthenticationConfigurer<>(new SecurityAuthenticationFilter(objectMapper)),
                        adapter -> adapter
                                .successHandler(createAuthenticationSuccessHandler())
                                .failureHandler(createAuthenticationFailureHandler())
                )
                .with(
                        new JwtAuthorityConfigurer(jwtProvider, userDetailsService),
                        Customizer.withDefaults()
                )
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .accessDeniedHandler(new SecurityAccessDeniedHandler())
                        .authenticationEntryPoint(createAuthenticationEntryPoint())
                );

        return http.getOrBuild();
    }

    private SecurityAuthenticationSuccessHandler createAuthenticationSuccessHandler() {
        return new SecurityAuthenticationSuccessHandler(
                objectMapper,
                jwtProvider
        );
    }

    private SecurityAuthenticationFailureHandler createAuthenticationFailureHandler() {
        return new SecurityAuthenticationFailureHandler();
    }

    private SecurityAuthenticationEntryPoint createAuthenticationEntryPoint() {
        return new SecurityAuthenticationEntryPoint(objectMapper);
    }
}
