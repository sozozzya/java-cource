package ru.senla.hotel.application.impl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.senla.hotel.application.service.TokenService;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Date;

@Service
public class JwtTokenService implements TokenService {

    private static final Logger log = LoggerFactory.getLogger(JwtTokenService.class);

    private final SecretKey secret = Keys.hmacShaKeyFor(
            "verySecretKeyThatIsAtLeast32BytesLong!".getBytes(StandardCharsets.UTF_8)
    );

    private final Duration ACCESS_EXP = Duration.ofMinutes(15);
    private final Duration REFRESH_EXP = Duration.ofDays(7);

    @Override
    public String generateAccessToken(String username, String role) {
        log.debug("Generating access token for {}", username);
        return buildToken(username, role, ACCESS_EXP);
    }

    @Override
    public String generateRefreshToken(String username) {
        log.debug("Generating refresh token for {}", username);
        return buildToken(username, null, REFRESH_EXP);
    }

    private String buildToken(String username, String role, Duration exp) {

        Date now = new Date();
        Date expiry = new Date(now.getTime() + exp.toMillis());

        JwtBuilder builder = Jwts.builder()
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(secret);

        if (role != null) {
            builder.claim("role", role);
        }

        return builder.compact();
    }

    @Override
    public String extractUsername(String token) {
        return parse(token).getSubject();
    }

    @Override
    public String extractRole(String token) {
        return parse(token).get("role", String.class);
    }

    @Override
    public boolean validate(String token) {
        try {
            parse(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            log.warn("JWT validation failed: {}", e.getMessage());
            return false;
        }
    }

    @Override
    public boolean isAccessToken(String token) {
        return extractRole(token) != null;
    }

    private Claims parse(String token) {
        return Jwts.parser()
                .verifyWith(secret)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
