package com.example.securitypilot.common.security.authentication;

import com.example.securitypilot.common.response.ErrorResponse;
import com.example.securitypilot.common.security.exception.InvalidAuthenticationArgumentException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

@Slf4j
@RequiredArgsConstructor
public class SecurityAuthenticationFailureHandler implements AuthenticationFailureHandler {

    private final ObjectMapper objectMapper;

    @Override
    public void onAuthenticationFailure(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException exception
    ) throws IOException {
        setResponse(response, exception);
        objectMapper.writeValue(response.getWriter(), ErrorResponse.of(exception.getMessage()));
    }

    private void setResponse(HttpServletResponse response, AuthenticationException exception) {
        response.setStatus(getHttpStatus(exception).value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
    }

    private HttpStatus getHttpStatus(AuthenticationException exception) {
        if (exception instanceof InvalidAuthenticationArgumentException) {
            return HttpStatus.BAD_REQUEST;
        }
        return HttpStatus.UNAUTHORIZED;
    }
}
