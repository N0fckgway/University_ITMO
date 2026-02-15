package dev.n0fckgway.lab4.services;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.Stateless;
import lombok.Getter;
import lombok.Setter;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Stateless
@Getter
@Setter
public class JwtService {
    private static final String FALLBACK_SECRET = "lab4-dev-secret-change-it-lab4-dev-secret-change-it";
    private SecretKey key;

    @PostConstruct
    public void init() {
        String keyStr = System.getenv("JWT_TOKEN");
        if (keyStr == null || keyStr.isBlank()) {
            keyStr = System.getProperty("jwt.token", FALLBACK_SECRET);
        }
        this.key = Keys.hmacShaKeyFor(keyStr.getBytes(StandardCharsets.UTF_8));
    }

    public String createToken(Long userId) {
        return Jwts.builder()
                .subject(String.valueOf(userId))
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000L * 60 * 60 * 4))
                .signWith(key)
                .compact();

    }

    public Claims extractClaims(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public Long extractUserId(String token) {
        Claims claims = extractClaims(token);
        return Long.parseLong(claims.getSubject());
    }


    public boolean checkValidToken(String token) {
        try {
            Claims claims = extractClaims(token);
            Date expiration = claims.getExpiration();
            return expiration == null || !expiration.before(new Date());
        } catch (Exception e) {
            return false;
        }
    }












}
