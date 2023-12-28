package com.example.securitypilot.common.security.exception.handler;

import static com.example.securitypilot.common.security.exception.SecurityErrorCode.SECURITY_ERROR_CODE_KEY;

import com.example.securitypilot.common.response.ErrorResponse;
import com.example.securitypilot.common.security.exception.SecurityErrorCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
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
        ErrorResponse errorResponse = createResponse(request, authException);
        sendErrorResponse(response, errorResponse);
    }

    private static ErrorResponse createResponse(
            HttpServletRequest request,
            AuthenticationException authException
    ) {
        Object attribute = request.getAttribute(SECURITY_ERROR_CODE_KEY);

        if (Objects.isNull(attribute)) {
            return ErrorResponse.of(authException.getMessage());
        }

        SecurityErrorCode securityErrorCode = (SecurityErrorCode) attribute;
        return ErrorResponse.of(securityErrorCode.getCode(), securityErrorCode.getMessage());
    }

    private void sendErrorResponse(HttpServletResponse response, ErrorResponse errorResponse)
            throws IOException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        objectMapper.writeValue(response.getWriter(), errorResponse);
    }
}
