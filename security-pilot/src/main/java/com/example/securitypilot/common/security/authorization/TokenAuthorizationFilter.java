package com.example.securitypilot.common.security.authorization;

import com.example.securitypilot.common.security.exception.ExpiredTokenException;
import com.example.securitypilot.common.security.exception.InvalidJwtException;
import com.example.securitypilot.common.security.jwt.JwtProvider;
import com.example.securitypilot.common.security.token.PrincipalDetails;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.NonNull;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@RequiredArgsConstructor
public class TokenAuthorizationFilter extends OncePerRequestFilter {

    private static final String GRANT_TYPE = "Bearer ";

    private final JwtProvider jwtProvider;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        String requestURI = request.getRequestURI();
        log.info("requestURI = {}", requestURI);

        String authorization = getAuthorization(request);
        if (isValidToken(authorization)) {
            String token = authorization.substring(GRANT_TYPE.length());
            log.info("accessToken = {}", token);

            try {
                String username = jwtProvider.getUsername(token);
                log.info("Authority username = {}", username);

                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                Authentication authenticated = createAuthentication(userDetails);
                setSecurityContext(authenticated);

                // todo: AuthorizationManager 구현
                PrincipalDetails principal = (PrincipalDetails) userDetails;
                if (!principal.isAccessUrl(requestURI)) {
                    throw new AccessDeniedException("[" + principal.getUsername() + "]" + requestURI + " 접근할 수 없는 URL 입니다.");
                }
            } catch (ExpiredJwtException e) {
                throw new ExpiredTokenException("만료된 토큰입니다.", e);
            } catch (UnsupportedJwtException | MalformedJwtException | SignatureException | IllegalArgumentException e) {
                throw new InvalidJwtException("유효하지 않은 토큰 입니다.", e);
            }
        }

        filterChain.doFilter(request, response);
    }

    private String getAuthorization(HttpServletRequest request) {
        return request.getHeader(HttpHeaders.AUTHORIZATION);
    }

    private boolean isValidToken(String token) {
        return StringUtils.hasText(token) && token.startsWith(GRANT_TYPE);
    }

    private UsernamePasswordAuthenticationToken createAuthentication(UserDetails userDetails) {
        return UsernamePasswordAuthenticationToken.authenticated(
                userDetails,
                userDetails.getPassword(),
                userDetails.getAuthorities()
        );
    }

    private void setSecurityContext(Authentication authenticated) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authenticated);
        SecurityContextHolder.setContext(context);
    }
}
