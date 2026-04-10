package ru.senla.hotel.application.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ru.senla.hotel.application.service.TokenService;
import ru.senla.hotel.domain.entity.AuthResult;
import ru.senla.hotel.domain.entity.User;
import ru.senla.hotel.domain.enums.Role;
import ru.senla.hotel.infrastructure.repository.UserRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthorizationServiceImplTest {

    @Mock
    private AuthenticationManager authManager;
    @Mock
    private TokenService tokenService;
    @Mock
    private TokenBlacklistService blacklistService;
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private AuthorizationServiceImpl service;

    @Test
    void loginShouldReturnTokensWhenCredentialsAreValid() {
        Authentication auth = org.mockito.Mockito.mock(Authentication.class);
        UserDetails principal = new org.springframework.security.core.userdetails.User(
                "john", "x", List.of(new SimpleGrantedAuthority("ROLE_USER")));
        when(authManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(auth);
        when(auth.getPrincipal()).thenReturn(principal);
        when(tokenService.generateAccessToken("john", "USER")).thenReturn("acc");
        when(tokenService.generateRefreshToken("john")).thenReturn("ref");

        AuthResult result = service.login("john", "pwd");

        assertEquals("acc", result.accessToken());
        assertEquals("ref", result.refreshToken());
    }

    @Test
    void loginShouldThrowBadCredentialsWhenAuthFails() {
        when(authManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("bad"));

        assertThrows(BadCredentialsException.class, () -> service.login("john", "bad"));
    }

    @Test
    void refreshShouldReturnNewTokensWhenRefreshValid() {
        User user = new User();
        user.setRole(Role.ADMIN);
        when(tokenService.validate("rt")).thenReturn(true);
        when(tokenService.extractUsername("rt")).thenReturn("kate");
        when(userRepository.findByUsername("kate")).thenReturn(Optional.of(user));
        when(tokenService.generateAccessToken("kate", "ADMIN")).thenReturn("newAcc");
        when(tokenService.generateRefreshToken("kate")).thenReturn("newRef");

        AuthResult result = service.refresh("rt");

        assertEquals("newAcc", result.accessToken());
        assertEquals("newRef", result.refreshToken());
    }

    @Test
    void refreshShouldThrowWhenTokenInvalid() {
        when(tokenService.validate("bad")).thenReturn(false);

        assertThrows(BadCredentialsException.class, () -> service.refresh("bad"));
    }

    @Test
    void logoutShouldBlacklistTokenWhenTokenPresent() {
        service.logout("token");

        verify(blacklistService).blacklist("token");
    }

    @Test
    void logoutShouldIgnoreBlankToken() {
        service.logout(" ");
    }
}
