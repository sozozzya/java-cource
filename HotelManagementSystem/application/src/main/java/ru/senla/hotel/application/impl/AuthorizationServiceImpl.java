package ru.senla.hotel.application.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.senla.hotel.application.service.AuthorizationService;
import ru.senla.hotel.application.service.TokenService;
import ru.senla.hotel.domain.entity.AuthResult;
import ru.senla.hotel.infrastructure.repository.UserRepository;

@Service
public class AuthorizationServiceImpl implements AuthorizationService {

    private static final Logger log = LoggerFactory.getLogger(AuthorizationServiceImpl.class);

    private final AuthenticationManager authManager;
    private final TokenService tokenService;
    private final TokenBlacklistService blacklistService;
    private final UserRepository userRepository;

    public AuthorizationServiceImpl(AuthenticationManager authManager,
                                    TokenService tokenService,
                                    TokenBlacklistService blacklistService,
                                    UserRepository userRepository) {
        this.authManager = authManager;
        this.tokenService = tokenService;
        this.blacklistService = blacklistService;
        this.userRepository = userRepository;
    }

    @Override
    public AuthResult login(String username, String password) {

        log.info("User login attempt: username='{}'", username);

        try {
            Authentication auth = authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );

            var user = (UserDetails) auth.getPrincipal();

            String role = user.getAuthorities()
                    .iterator()
                    .next()
                    .getAuthority()
                    .replace("ROLE_", "");

            String accessToken = tokenService.generateAccessToken(username, role);
            String refreshToken = tokenService.generateRefreshToken(username);

            log.info("User authenticated: username='{}'", username);

            return new AuthResult(accessToken, refreshToken);
        } catch (BadCredentialsException ex) {
            log.warn("Invalid credentials: username='{}'", username);
            throw ex;
        } catch (Exception ex) {
            log.error("Login failed: username='{}'", username, ex);
            throw new RuntimeException("Authentication failed", ex);
        }
    }

    @Override
    public AuthResult refresh(String refreshToken) {

        log.debug("Refreshing token");

        if (!tokenService.validate(refreshToken)) {
            log.warn("Invalid refresh token");
            throw new BadCredentialsException("Invalid refresh token");
        }

        String username = tokenService.extractUsername(refreshToken);

        var user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));

        String role = user.getRole().name();

        String newAccess = tokenService.generateAccessToken(username, role);
        String newRefresh = tokenService.generateRefreshToken(username);

        log.info("Token refreshed for user='{}'", username);

        return new AuthResult(newAccess, newRefresh);
    }

    @Override
    public void logout(String token) {

        if (token == null || token.isBlank()) {
            log.warn("Logout without token");
            return;
        }

        blacklistService.blacklist(token);

        log.info("Token revoked");
    }
}
