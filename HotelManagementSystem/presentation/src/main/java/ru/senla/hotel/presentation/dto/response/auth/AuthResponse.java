package ru.senla.hotel.presentation.dto.response.auth;

public record AuthResponse(
        String accessToken,
        String refreshToken
) {
}
