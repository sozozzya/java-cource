package ru.senla.hotel.presentation.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.senla.hotel.application.impl.TokenBlacklistService;
import ru.senla.hotel.application.service.TokenService;

import java.io.IOException;
import java.util.List;

@Component
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(JwtAuthorizationFilter.class);

    private final TokenService tokenService;
    private final TokenBlacklistService blacklist;

    public JwtAuthorizationFilter(TokenService tokenService,
                                  TokenBlacklistService blacklist) {
        this.tokenService = tokenService;
        this.blacklist = blacklist;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest req,
            HttpServletResponse res,
            FilterChain chain
    ) throws IOException, ServletException {

        String header = req.getHeader("Authorization");

        if (header != null && header.startsWith("Bearer ")) {

            String token = header.substring(7);

            if (blacklist.isBlacklisted(token)) {
                log.warn("Blocked blacklisted token");
                chain.doFilter(req, res);
                return;
            }

            if (tokenService.validate(token)) {

                String username = tokenService.extractUsername(token);
                String role = tokenService.extractRole(token);

                if (role != null) {
                    var authorities = List.of(
                            new SimpleGrantedAuthority("ROLE_" + role)
                    );

                    var auth = new UsernamePasswordAuthenticationToken(
                            username,
                            null,
                            authorities
                    );

                    SecurityContextHolder.getContext().setAuthentication(auth);
                    log.debug("User authenticated: {}", username);
                }
            } else {
                log.warn("Invalid JWT token");
            }
        }

        chain.doFilter(req, res);
    }
}
