package ru.senla.hotel.application.service;

import ru.senla.hotel.domain.entity.AuthResult;

public interface AuthorizationService {
    AuthResult login(String username, String password);

    AuthResult refresh(String refreshToken);

    void logout(String token);
}
