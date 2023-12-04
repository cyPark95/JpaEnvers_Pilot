//package com.example.securitypilot;
//
//import com.example.securitypilot.common.security.jwt.JwtProvider;
//import com.security.securitypilot.common.config.SecurityProcessor;
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.util.StringUtils;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//@Slf4j
//@RequiredArgsConstructor
//public class JwtFilter extends OncePerRequestFilter {
//
//    private static final String AUTHORIZATION_HEADER = "Authorization";
//
//    private final JwtProvider jwtProvider;
//    private final SecurityProcessor securityProcessor;
//
//    @Override
//    protected void doFilterInternal(
//            HttpServletRequest request,
//            HttpServletResponse response,
//            FilterChain filterChain
//    ) throws ServletException, IOException {
//        String requestURI = request.getRequestURI();
//        log.info("Request URL = {}", requestURI);
//
//        String authorization = getAuthorization(request);
//        String token = jwtProvider.resolveToken(authorization);
//        log.info("Request Token = {}", token);
//
//        if (isValidToken(token)) {
//            Long userId = jwtProvider.getUserId(token);
//
//            if (securityProcessor.process(requestURI, userId)) {
//                Authentication authentication = jwtProvider.getAuthentication(userId);
//                SecurityContextHolder.getContext().setAuthentication(authentication);
//            }
//        }
//
//        filterChain.doFilter(request, response);
//    }
//
//    private String getAuthorization(HttpServletRequest request) {
//        return request.getHeader(AUTHORIZATION_HEADER);
//    }
//
//    private boolean isValidToken(String token) {
//        return StringUtils.hasText(token) && jwtProvider.validate(token);
//    }
//}
