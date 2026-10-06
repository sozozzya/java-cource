package ru.senla.hotel.infrastructure.repository;

import ru.senla.hotel.domain.entity.User;

import java.util.Optional;

public interface UserRepository {
    Optional<User> findByUsername(String username);

    User save(User user);
}
