package ru.senla.hotel.presentation.api.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.senla.hotel.application.service.AuthorizationService;
import ru.senla.hotel.application.service.RegistrationService;
import ru.senla.hotel.domain.entity.User;
import ru.senla.hotel.presentation.dto.request.auth.LoginRequest;
import ru.senla.hotel.presentation.dto.request.auth.RefreshRequest;
import ru.senla.hotel.presentation.dto.request.auth.RegisterRequest;
import ru.senla.hotel.presentation.dto.response.auth.AuthResponse;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthorizationController {

    private final AuthorizationService authService;
    private final RegistrationService registrationService;

    public AuthorizationController(
            AuthorizationService authService,
            RegistrationService registrationService
    ) {
        this.authService = authService;
        this.registrationService = registrationService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody @Valid LoginRequest request) {

        var result = authService.login(request.username(), request.password());

        return ResponseEntity.ok(new AuthResponse(result.accessToken(), result.refreshToken())
        );
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid RegisterRequest request) {

        User user = registrationService.register(
                request.username(),
                request.password()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                "username", user.getUsername(),
                "role", user.getRole().name()
        ));
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(@RequestBody RefreshRequest request) {

        var result = authService.refresh(request.refreshToken());

        return ResponseEntity.ok(
                new AuthResponse(result.accessToken(), result.refreshToken())
        );
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request) {

        String header = request.getHeader("Authorization");

        String token = (header != null && header.startsWith("Bearer "))
                ? header.substring(7)
                : null;

        authService.logout(token);

        return ResponseEntity.noContent().build();
    }
}
