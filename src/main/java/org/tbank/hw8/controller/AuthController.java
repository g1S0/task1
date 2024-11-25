package org.tbank.hw8.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.tbank.hw8.dto.AuthenticationRequestDto;
import org.tbank.hw8.dto.AuthenticationResponseDto;
import org.tbank.hw8.dto.RegisterRequestDto;
import org.tbank.hw8.entity.Role;
import org.tbank.hw8.entity.User;
import org.tbank.hw8.service.AuthService;

@RestController
@RequestMapping("/api/v1/auth")
@Slf4j
public class AuthController {

    private final AuthService service;

    public AuthController(AuthService service) {
        this.service = service;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponseDto> addNewUser(
            @RequestBody @Valid RegisterRequestDto registerRequest
    ) {
        User user = User.builder()
                .firstName(registerRequest.getFirstName())
                .secondName(registerRequest.getSecondName())
                .email(registerRequest.getEmail())
                .password(registerRequest.getPassword())
                .role(Role.USER)
                .build();

        return ResponseEntity.ok(service.register(user));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponseDto> authenticate(
            @RequestBody @Valid AuthenticationRequestDto request
    ) {
        if (request.getRememberMe() == null) {
            request.setRememberMe(false);
        }

        return ResponseEntity.ok(service.authenticate(request));
    }
}
