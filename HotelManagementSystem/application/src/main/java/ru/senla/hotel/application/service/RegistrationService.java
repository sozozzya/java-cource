package ru.senla.hotel.application.service;

import ru.senla.hotel.domain.entity.User;
import ru.senla.hotel.domain.enums.Role;

public interface RegistrationService {

    User register(String username, String rawPassword);

    User registerWithRole(String username, String rawPassword, Role role);
}
