package ru.senla.hotel.application.impl;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TokenBlacklistServiceTest {

    private final TokenBlacklistService service = new TokenBlacklistService();

    @Test
    void blacklistShouldAddToken() {
        service.blacklist("t1");

        assertTrue(service.isBlacklisted("t1"));
    }

    @Test
    void isBlacklistedShouldReturnFalseForUnknownToken() {
        assertFalse(service.isBlacklisted("unknown"));
    }
}
