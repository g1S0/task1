package org.tbank.hw8.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;
import org.tbank.hw8.exception.InvalidAuthorizationHeaderException;
import org.tbank.hw8.repository.TokenRepository;


@Service
@RequiredArgsConstructor
@Slf4j
public class LogoutService implements LogoutHandler {

    private final TokenRepository tokenRepository;

    @Override
    public void logout(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) {
        final String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            log.warn("Missing or invalid Authorization header in logout request");
            throw new InvalidAuthorizationHeaderException("Authorization header is missing or invalid");
        }

        String jwt = authHeader.substring(7);
        log.debug("Processing logout for token: {}", jwt);

        var storedToken = tokenRepository.findTokenByValue(jwt).orElse(null);
        if (storedToken != null) {
            log.debug("Revoking and expiring token: {}", jwt);
            storedToken.setRevoked(true);
            tokenRepository.save(storedToken);
            SecurityContextHolder.clearContext();
            log.info("Successfully logged out user with token: {}", jwt);
        } else {
            log.warn("Token not found: {}", jwt);
        }
    }
}

