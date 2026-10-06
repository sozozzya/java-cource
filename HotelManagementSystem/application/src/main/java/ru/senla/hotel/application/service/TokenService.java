package ru.senla.hotel.application.service;

public interface TokenService {

    String generateAccessToken(String username, String role);

    String generateRefreshToken(String username);

    String extractUsername(String token);

    String extractRole(String token);

    boolean validate(String token);

    boolean isAccessToken(String token);
}
