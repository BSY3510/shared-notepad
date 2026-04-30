package com.bsy.shared_notepad.config;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.util.Date;
import javax.crypto.SecretKey;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class JwtProvider {

    private final SecretKey secretKey;
    private final long accessTokenExpiration;
    private final long refreshTokenExpiration;

    public JwtProvider(
        @Value("${jwt.secret-key}") String secretKeyString,
        @Value("${jwt.access-token-expiration}") long accessTokenExpiration,
        @Value("${jwt.refresh-token-expiration}") long refreshTokenExpiration
    ) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKeyString);
        this.secretKey = Keys.hmacShaKeyFor(keyBytes);
        this.accessTokenExpiration = accessTokenExpiration;
        this.refreshTokenExpiration = refreshTokenExpiration;
    }

    public String createAccessToken(String email) {
        return createToken(email, accessTokenExpiration);
    }

    public String createRefreshToken(String email) {
        return createToken(email, refreshTokenExpiration);
    }

    private String createToken(String email, long expirationTime) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + expirationTime);
        return Jwts.builder()
            .subject(email)
            .issuedAt(now)
            .expiration(validity)
            .signWith(secretKey)
            .compact();
    }
}
