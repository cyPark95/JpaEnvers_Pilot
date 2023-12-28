package com.example.securitypilot.common.security.authorization;

import static com.example.securitypilot.common.security.exception.SecurityErrorCode.SECURITY_ERROR_CODE_KEY;
import static com.example.securitypilot.common.security.exception.SecurityErrorCode.TOKEN_EXPIRED;

import com.example.securitypilot.common.security.exception.InvalidAuthenticationArgumentException;
import com.example.securitypilot.common.security.exception.InvalidJwtException;
import com.example.securitypilot.common.security.jwt.JwtProvider;
import com.example.securitypilot.domain.auth.token.PrincipalDetails;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
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
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private static final String AUTHORIZATION_HEADER = "Authorization";
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

        resolveToken(request).ifPresent(token -> {
            log.info("accessToken = {}", token);

            try {
                String username = jwtProvider.getUsername(token);
                log.info("Authority username = {}", username);

                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                PrincipalDetails principal = (PrincipalDetails) userDetails;
                if (!principal.isAccessUrl(requestURI)) {
                    throw new InvalidAuthenticationArgumentException("잘못된 접근입니다.");
                }

                Authentication authenticated = createAuthentication(userDetails);
                setSecurityContext(authenticated);
            } catch (ExpiredJwtException e) {
                request.setAttribute(SECURITY_ERROR_CODE_KEY, TOKEN_EXPIRED);
            } catch (UnsupportedJwtException | MalformedJwtException | SignatureException | IllegalArgumentException e) {
                throw new InvalidJwtException("유효하지 않은 토큰 입니다.", e);
            }
        });

        filterChain.doFilter(request, response);
    }

    private Optional<String> resolveToken(HttpServletRequest request) {
        String token = getAuthorization(request);
        if (isValidToken(token)) {
            return Optional.of(token.substring(GRANT_TYPE.length()));
        }
        return Optional.empty();
    }

    private String getAuthorization(HttpServletRequest request) {
        return request.getHeader(AUTHORIZATION_HEADER);
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
