package ru.senla.hotel.application.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.senla.hotel.domain.entity.User;
import ru.senla.hotel.domain.enums.Role;
import ru.senla.hotel.infrastructure.repository.UserRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RegistrationServiceImplTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private RegistrationServiceImpl service;

    @Test
    void registerShouldCreateUserWithDefaultRole() {
        User saved = new User();
        saved.setUsername("john");
        saved.setRole(Role.USER);

        when(userRepository.findByUsername("john")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("pwd")).thenReturn("enc");
        when(userRepository.save(any(User.class))).thenReturn(saved);

        User result = service.register("john", "pwd");

        assertEquals("john", result.getUsername());
        assertEquals(Role.USER, result.getRole());
    }

    @Test
    void registerShouldThrowWhenUserAlreadyExists() {
        when(userRepository.findByUsername("john")).thenReturn(Optional.of(new User()));

        assertThrows(IllegalArgumentException.class, () -> service.register("john", "pwd"));
    }

    @Test
    void registerWithRoleShouldCreateUserWhenRoleProvided() {
        User saved = new User();
        saved.setUsername("admin");
        saved.setRole(Role.ADMIN);

        when(userRepository.findByUsername("admin")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("pwd")).thenReturn("enc");
        when(userRepository.save(any(User.class))).thenReturn(saved);

        User result = service.registerWithRole("admin", "pwd", Role.ADMIN);

        assertEquals(Role.ADMIN, result.getRole());
    }

    @Test
    void registerWithRoleShouldThrowWhenUserExists() {
        when(userRepository.findByUsername("admin")).thenReturn(Optional.of(new User()));

        assertThrows(IllegalArgumentException.class,
                () -> service.registerWithRole("admin", "pwd", Role.ADMIN));
    }
}
