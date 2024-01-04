package com.example.securitypilot.common.security.authentication;

import com.example.securitypilot.common.security.authentication.dto.LoginRequest;
import com.example.securitypilot.common.security.exception.InvalidAuthenticationArgumentException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.ValueInstantiationException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Slf4j
public class SecurityAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private static final AntPathRequestMatcher DEFAULT_ANT_PATH_REQUEST_MATCHER =
            new AntPathRequestMatcher("/login", "POST");

    private final ObjectMapper objectMapper;

    public SecurityAuthenticationFilter(ObjectMapper objectMapper) {
        super(DEFAULT_ANT_PATH_REQUEST_MATCHER);
        this.objectMapper = objectMapper;
    }

    @Override
    public Authentication attemptAuthentication(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws AuthenticationException {
        LoginRequest loginRequest = getLoginInfo(request);
        String email = loginRequest.email();
        String password = loginRequest.password();

        UsernamePasswordAuthenticationToken authRequest =
                UsernamePasswordAuthenticationToken.unauthenticated(email, password);
        return getAuthenticationManager().authenticate(authRequest);
    }

    private LoginRequest getLoginInfo(HttpServletRequest request) {
        try {
            return objectMapper.readValue(request.getReader(), LoginRequest.class);
        } catch (IOException e) {
            String errorMessage = getErrorMessage(e);
            throw new InvalidAuthenticationArgumentException(errorMessage);
        }
    }

    private String getErrorMessage(IOException e) {
        if (e instanceof ValueInstantiationException) {
            return e.getCause().getMessage();
        }
        log.error(e.getMessage());
        return "잘못된 요청입니다.";
    }
}
