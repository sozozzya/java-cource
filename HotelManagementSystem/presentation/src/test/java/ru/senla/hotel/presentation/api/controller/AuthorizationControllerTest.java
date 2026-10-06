package ru.senla.hotel.presentation.api.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.senla.hotel.application.service.AuthorizationService;
import ru.senla.hotel.application.service.RegistrationService;
import ru.senla.hotel.domain.entity.AuthResult;
import ru.senla.hotel.domain.entity.User;
import ru.senla.hotel.domain.enums.Role;
import ru.senla.hotel.presentation.dto.request.auth.LoginRequest;
import ru.senla.hotel.presentation.dto.request.auth.RefreshRequest;
import ru.senla.hotel.presentation.dto.request.auth.RegisterRequest;
import ru.senla.hotel.presentation.dto.response.auth.AuthResponse;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthorizationControllerTest {

    @Mock
    private AuthorizationService authService;
    @Mock
    private RegistrationService registrationService;
    @InjectMocks
    private AuthorizationController controller;

    @Test
    void loginShouldReturnTokensWhenCredentialsAreValid() {
        LoginRequest request = new LoginRequest("john", "secret");
        when(authService.login("john", "secret"))
                .thenReturn(new AuthResult("access", "refresh"));

        ResponseEntity<AuthResponse> response = controller.login(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("access", response.getBody().accessToken());
        assertEquals("refresh", response.getBody().refreshToken());
    }

    @Test
    void loginShouldPropagateExceptionWhenServiceFails() {
        LoginRequest request = new LoginRequest("john", "bad");
        when(authService.login("john", "bad"))
                .thenThrow(new IllegalArgumentException("invalid"));

        assertThrows(IllegalArgumentException.class, () -> controller.login(request));
    }

    @Test
    void registerShouldCreateUserWhenDataIsValid() {
        RegisterRequest request = new RegisterRequest("kate", "pwd");
        User user = new User();
        user.setUsername("kate");
        user.setRole(Role.USER);
        when(registrationService.register("kate", "pwd")).thenReturn(user);

        ResponseEntity<?> response = controller.register(request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertInstanceOf(Map.class, response.getBody());
    }

    @Test
    void registerShouldPropagateExceptionWhenServiceFails() {
        RegisterRequest request = new RegisterRequest("kate", "pwd");
        when(registrationService.register("kate", "pwd"))
                .thenThrow(new IllegalStateException("exists"));

        assertThrows(IllegalStateException.class, () -> controller.register(request));
    }

    @Test
    void refreshShouldReturnNewTokensWhenRefreshTokenIsValid() {
        RefreshRequest request = new RefreshRequest("old-refresh");
        when(authService.refresh("old-refresh"))
                .thenReturn(new AuthResult("new-access", "new-refresh"));

        ResponseEntity<AuthResponse> response = controller.refresh(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("new-access", response.getBody().accessToken());
        assertEquals("new-refresh", response.getBody().refreshToken());
    }

    @Test
    void refreshShouldPropagateExceptionWhenServiceFails() {
        RefreshRequest request = new RefreshRequest("broken");
        when(authService.refresh("broken")).thenThrow(new IllegalArgumentException("bad token"));

        assertThrows(IllegalArgumentException.class, () -> controller.refresh(request));
    }

    @Test
    void logoutShouldReturnNoContentWhenTokenPresent() {
        HttpServletRequest servletRequest = org.mockito.Mockito.mock(HttpServletRequest.class);
        when(servletRequest.getHeader("Authorization")).thenReturn("Bearer abc");

        ResponseEntity<Void> response = controller.logout(servletRequest);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(authService).logout("abc");
    }

    @Test
    void logoutShouldPropagateExceptionWhenServiceFailsForNullToken() {
        HttpServletRequest servletRequest = org.mockito.Mockito.mock(HttpServletRequest.class);
        when(servletRequest.getHeader("Authorization")).thenReturn(null);
        doThrow(new IllegalStateException("cannot logout")).when(authService).logout(null);

        assertThrows(IllegalStateException.class, () -> controller.logout(servletRequest));
    }
}
