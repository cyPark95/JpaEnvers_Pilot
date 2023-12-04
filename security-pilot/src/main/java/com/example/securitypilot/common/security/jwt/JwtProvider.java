package com.example.securitypilot.common.security.jwt;

import static io.jsonwebtoken.SignatureAlgorithm.HS256;
import static org.springframework.security.config.Elements.JWT;

import com.example.securitypilot.domain.auth.token.Token;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import java.security.Key;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import javax.crypto.SecretKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class JwtProvider {

    private static final int MILLISECONDS_TO_SECONDS = 1000;
    private static final int TOKEN_REFRESH_INTERVAL = MILLISECONDS_TO_SECONDS * 24;
    private static final String AUTHORITIES_KEY = "role";

    private final Key key;
    private final String grantType;
    private final Long tokenValidateInSeconds;
    private final long accessTokenExpiredTime;
    private final long refreshTokenExpiredTime;

    public JwtProvider(
            @Value("${jwt.secret}") String secretKey,
            @Value("${jwt.grand-type}") String grantType,
            @Value("${jwt.token-validate-in-seconds}") Long tokenValidateInSeconds
    ) {
        this.key = getSecretKey(secretKey);
        this.grantType = grantType;
        this.tokenValidateInSeconds = tokenValidateInSeconds;
        this.accessTokenExpiredTime = tokenValidateInSeconds * MILLISECONDS_TO_SECONDS;
        this.refreshTokenExpiredTime = tokenValidateInSeconds * TOKEN_REFRESH_INTERVAL;
    }

    public Token generateToken(String email) {
        return Token.builder()
                .accessToken(createToken(email, refreshTokenExpiredTime))
                .grantType(grantType)
                .expiresIn(tokenValidateInSeconds)
                .refreshToken(createToken(email, accessTokenExpiredTime))
                .build();
    }

    public String getUserId(String token)
            throws ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, SignatureException, IllegalArgumentException {
        Claims claims = getClaims(token);
        return claims.getSubject();
    }

    private SecretKey getSecretKey(String secretKey) {
        String base64EncodedSecretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
        byte[] keyBytes = Decoders.BASE64.decode(base64EncodedSecretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private String createToken(String email, long expiredTime) {
        Date now = new Date();
        Date expiredDate = new Date(System.currentTimeMillis() + expiredTime);

        return Jwts.builder()
                .setHeader(createHeader())
                .setSubject(email)
                .setIssuedAt(now)
                .setExpiration(expiredDate)
                .signWith(key, HS256)
                .compact();
    }

    private Map<String, Object> createHeader() {
        Map<String, Object> header = new HashMap<>();
        header.put("alg", HS256.getValue());
        header.put("typ", JWT);
        return header;
    }

    private Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Collection<? extends GrantedAuthority> getGrantedAuthorities(Claims claims) {
        return Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }
}
