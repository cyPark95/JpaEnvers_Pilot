package com.example.securitypilot.common.security.exception.handler;

import com.example.securitypilot.common.response.ErrorResponse;
import com.example.securitypilot.common.security.exception.DefaultAuthenticationException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

@Slf4j
@RequiredArgsConstructor
public class SecurityAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException
    ) throws IOException {
        log.error("[ERROR] AuthenticationEntryPoint = {}", authException.getMessage());
        ErrorResponse errorResponse = createResponse(authException);
        sendErrorResponse(response, errorResponse);
    }

    private ErrorResponse createResponse(AuthenticationException authException) {
        if (authException instanceof DefaultAuthenticationException e) {
            return ErrorResponse.of(e.getCode(), e.getMessage());
        }
        return ErrorResponse.of(authException.getMessage());
    }

    private void sendErrorResponse(HttpServletResponse response, ErrorResponse errorResponse)
            throws IOException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        PrintWriter writer = response.getWriter();
        objectMapper.writeValue(writer, errorResponse);
    }
}
