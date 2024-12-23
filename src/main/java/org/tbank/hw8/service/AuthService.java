package org.tbank.hw8.service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.tbank.hw8.dto.AuthenticationRequestDto;
import org.tbank.hw8.dto.AuthenticationResponseDto;
import org.tbank.hw8.dto.ChangePasswordRequestDto;
import org.tbank.hw8.entity.Token;
import org.tbank.hw8.entity.User;
import org.tbank.hw8.repository.TokenRepository;
import org.tbank.hw8.repository.UserRepository;

import java.security.Principal;

@Service
@AllArgsConstructor
@Slf4j
public class AuthService {
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Transactional
    public AuthenticationResponseDto register(User user) {
        log.debug("Registering user with email: {}", user.getEmail());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        var savedUser = userRepository.save(user);
        var token = jwtService.generateToken(user);
        saveUserToken(savedUser, token);

        log.info("User registered successfully: {}", user.getEmail());
        return AuthenticationResponseDto.builder()
                .token(token)
                .build();
    }

    @Transactional
    public AuthenticationResponseDto authenticate(AuthenticationRequestDto request) {
        log.debug("Authenticating user with email: {}", request.getEmail());
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));
        var token = jwtService.generateTokenWithRememberMeParameters(user, request.getRememberMe());
        revokeAllUserTokens(user);
        saveUserToken(user, token);

        log.info("User authenticated successfully: {}", request.getEmail());
        return AuthenticationResponseDto.builder()
                .token(token)
                .build();
    }

    private void saveUserToken(User user, String jwtToken) {
        log.debug("Saving token for user: {}", user.getEmail());
        var token = Token.builder()
                .user(user)
                .token(jwtToken)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }

    private void revokeAllUserTokens(User user) {
        log.debug("Revoking all tokens for user: {}", user.getEmail());
        var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }

    public void changePassword(ChangePasswordRequestDto request, Principal connectedUser) {
        var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
        log.debug("Attempting to change password for user: {}", user.getEmail());

        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            log.error("Failed to change password: current password does not match for user: {}", user.getEmail());
            throw new IllegalStateException("Wrong password");
        }

        if (!request.getNewPassword().equals(request.getConfirmationPassword())) {
            log.error("Failed to change password: new password and confirmation do not match for user: {}", user.getEmail());
            throw new IllegalStateException("Password are not the same");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
        log.info("Password successfully changed for user: {}", user.getEmail());
    }
}

