package com.example.securitypilot.common.security.jwt;

import static io.jsonwebtoken.SignatureAlgorithm.HS256;
import static org.springframework.security.config.Elements.JWT;

import com.example.securitypilot.common.security.token.Token;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.crypto.SecretKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class JwtProvider {

    private static final int MILLISECONDS_TO_SECONDS = 1000;
    private static final int TOKEN_REFRESH_INTERVAL = MILLISECONDS_TO_SECONDS * 24;

    private final Key key;
    private final String grantType;
    private final Long accessTokenExpiredTime;
    private final Long refreshTokenExpiredTime;

    public JwtProvider(
            @Value("${jwt.secret}") String secretKey,
            @Value("${jwt.grant-type}") String grantType,
            @Value("${jwt.token-validate-in-seconds}") Long tokenValidateInSeconds
    ) {
        this.key = getSecretKey(secretKey);
        this.grantType = grantType;
        this.accessTokenExpiredTime = tokenValidateInSeconds * MILLISECONDS_TO_SECONDS;
        this.refreshTokenExpiredTime = tokenValidateInSeconds * TOKEN_REFRESH_INTERVAL;
    }

    public Token generateToken(String email) {
        return Token.builder()
                .grantType(grantType)
                .accessToken(createToken(email, accessTokenExpiredTime))
                .accessTokenExpiresIn(accessTokenExpiredTime)
                .refreshToken(createToken(email, refreshTokenExpiredTime))
                .refreshTokenExpiresIn(refreshTokenExpiredTime)
                .build();
    }

    public String getUsername(String token)
            throws ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, SignatureException, IllegalArgumentException {
        Claims claims = getClaims(token);
        return claims.getSubject();
    }

    private SecretKey getSecretKey(String secretKey) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private String createToken(String email, Long expiredTime) {
        Date now = new Date();
        Date expiredDate = new Date(now.getTime() + expiredTime);

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
}
