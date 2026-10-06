package ru.senla.hotel.domain.entity;

public record AuthResult(
        String accessToken,
        String refreshToken
) {
}
