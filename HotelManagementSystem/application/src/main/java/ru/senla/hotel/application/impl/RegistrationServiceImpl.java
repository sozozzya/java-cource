package ru.senla.hotel.application.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.senla.hotel.application.service.RegistrationService;
import ru.senla.hotel.domain.entity.User;
import ru.senla.hotel.domain.enums.Role;
import ru.senla.hotel.infrastructure.repository.UserRepository;

@Service
@Transactional
public class RegistrationServiceImpl implements RegistrationService {

    private static final Logger log = LoggerFactory.getLogger(RegistrationServiceImpl.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public RegistrationServiceImpl(UserRepository userRepository,
                                   PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User register(String username, String rawPassword) {
        return registerWithRole(username, rawPassword, Role.USER);
    }

    @Override
    public User registerWithRole(String username, String rawPassword, Role role) {
        log.info("Registering new user: username='{}', role={}", username, role);

        if (userRepository.findByUsername(username).isPresent()) {
            log.warn("User '{}' already exists", username);
            throw new IllegalArgumentException("User already exists");
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(rawPassword));
        user.setRole(role);

        User saved = userRepository.save(user);
        log.info("User successfully created: id={}, username='{}'", saved.getId(), saved.getUsername());
        return saved;
    }
}
