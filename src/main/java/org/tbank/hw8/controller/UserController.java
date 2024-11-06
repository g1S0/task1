package org.tbank.hw8.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.tbank.hw8.dto.ChangePasswordRequestDto;
import org.tbank.hw8.exception.InvalidConfirmationCodeException;
import org.tbank.hw8.service.impl.AuthService;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/user")
@Slf4j
public class UserController {

    private final AuthService service;

    public UserController(AuthService service) {
        this.service = service;
    }

    @PostMapping("/reset-password")
    public ResponseEntity<Void> changePassword(
            @RequestBody ChangePasswordRequestDto request,
            Principal connectedUser
    ) {
        if (!request.getCode().equals("0000")) {
            throw new InvalidConfirmationCodeException("Code is invalid");
        }

        service.changePassword(request, connectedUser);

        return ResponseEntity.ok().build();
    }
}
