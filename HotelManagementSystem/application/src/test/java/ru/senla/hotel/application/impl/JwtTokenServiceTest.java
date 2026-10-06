package ru.senla.hotel.application.impl;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JwtTokenServiceTest {

    private final JwtTokenService service = new JwtTokenService();

    @Test
    void generateAccessTokenShouldCreateTokenWithRole() {
        String token = service.generateAccessToken("john", "USER");

        assertNotNull(token);
        assertEquals("john", service.extractUsername(token));
        assertEquals("USER", service.extractRole(token));
        assertTrue(service.isAccessToken(token));
    }

    @Test
    void generateAccessTokenShouldBeInvalidForCorruptedValue() {
        String token = service.generateAccessToken("john", "USER");

        assertFalse(service.validate(token + "x"));
    }

    @Test
    void generateRefreshTokenShouldCreateTokenWithoutRole() {
        String token = service.generateRefreshToken("kate");

        assertNotNull(token);
        assertEquals("kate", service.extractUsername(token));
        assertFalse(service.isAccessToken(token));
    }

    @Test
    void extractUsernameShouldThrowForInvalidToken() {
        org.junit.jupiter.api.Assertions.assertThrows(RuntimeException.class,
                () -> service.extractUsername("bad"));
    }

    @Test
    void validateShouldReturnFalseForMalformedToken() {
        assertFalse(service.validate("bad-token"));
    }

    @Test
    void isAccessTokenShouldReturnFalseWhenRoleMissing() {
        String refresh = service.generateRefreshToken("sam");

        assertFalse(service.isAccessToken(refresh));
    }
}
