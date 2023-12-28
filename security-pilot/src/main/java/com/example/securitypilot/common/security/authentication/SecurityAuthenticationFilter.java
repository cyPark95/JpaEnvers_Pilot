package com.example.securitypilot.common.security.authentication;

import com.example.securitypilot.common.security.authentication.dto.LoginRequest;
import com.example.securitypilot.common.security.exception.InvalidAuthenticationArgumentException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.StringUtils;

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
    ) throws AuthenticationException, IOException {
        LoginRequest loginRequest = objectMapper.readValue(request.getReader(), LoginRequest.class);
        validateLoginInfo(loginRequest);

        String email = loginRequest.email();
        String password = loginRequest.password();

        UsernamePasswordAuthenticationToken authRequest =
                UsernamePasswordAuthenticationToken.unauthenticated(email, password);
        return getAuthenticationManager().authenticate(authRequest);
    }

    private void validateLoginInfo(LoginRequest loginRequest) {
        if (!StringUtils.hasText(loginRequest.email())) {
            throw new InvalidAuthenticationArgumentException("이메일은 필수 값 입니다.");
        }

        if (!StringUtils.hasText(loginRequest.password())) {
            throw new InvalidAuthenticationArgumentException("비밀번호는 필수 값 입니다.");
        }
    }
}
