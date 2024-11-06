package org.tbank.hw8.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.tbank.hw8.dto.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AuthControllerIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    private String registerUser() {
        RegisterRequestDto registerRequest = new RegisterRequestDto();
        registerRequest.setFirstName("John");
        registerRequest.setSecondName("Doe");
        registerRequest.setEmail("john.doe@example.com");
        registerRequest.setPassword("Password@123)");

        ResponseEntity<AuthenticationResponseDto> response = restTemplate
                .postForEntity("/api/v1/auth/register", registerRequest, AuthenticationResponseDto.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        return response.getBody().getToken();
    }

    private void resetPassword(String token, ChangePasswordRequestDto changePasswordRequest) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);

        ResponseEntity<Void> resetPasswordResponse = restTemplate.exchange(
                "/api/v1/user/reset-password",
                HttpMethod.POST,
                new HttpEntity<>(changePasswordRequest, headers),
                Void.class
        );

        assertEquals(HttpStatus.OK, resetPasswordResponse.getStatusCode());
    }

    private ResponseEntity<AuthenticationResponseDto> authenticate(String email, String password) {
        AuthenticationRequestDto authRequest = new AuthenticationRequestDto();
        authRequest.setEmail(email);
        authRequest.setPassword(password);
        return restTemplate.postForEntity("/api/v1/auth/authenticate", authRequest, AuthenticationResponseDto.class);
    }

    @Test
    @DirtiesContext
    void testRegister() {
        String token = registerUser();
        assertNotNull(token);
    }

    @Test
    @DirtiesContext
    void testRegisterAndGetCurrencyRate() {
        String token = registerUser();
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);

        ResponseEntity<CurrencyRateDto> currencyRateResponse = restTemplate
                .exchange("/currencies/rates/USD", HttpMethod.GET, new HttpEntity<>(headers), CurrencyRateDto.class);

        assertEquals(HttpStatus.OK, currencyRateResponse.getStatusCode());
        assertNotNull(currencyRateResponse.getBody());
    }

    @Test
    @DirtiesContext
    void testResetPassword() {
        String token = registerUser();

        ChangePasswordRequestDto changePasswordRequest = new ChangePasswordRequestDto();
        changePasswordRequest.setCode("0000");
        changePasswordRequest.setCurrentPassword("Password@123)");
        changePasswordRequest.setNewPassword("NewPassword@123)");
        changePasswordRequest.setConfirmationPassword("NewPassword@123)");

        resetPassword(token, changePasswordRequest);

        ResponseEntity<AuthenticationResponseDto> oldTokenResponse = authenticate("john.doe@example.com", "Password@123)");
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, oldTokenResponse.getStatusCode());

        ResponseEntity<AuthenticationResponseDto> newAuthResponse = authenticate("john.doe@example.com", "NewPassword@123)");
        assertEquals(HttpStatus.OK, newAuthResponse.getStatusCode());
        assertNotNull(newAuthResponse.getBody());
        String newToken = newAuthResponse.getBody().getToken();
        assertNotNull(newToken);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(newToken);
        ResponseEntity<CurrencyRateDto> currencyRateResponse = restTemplate
                .exchange("/currencies/rates/USD", HttpMethod.GET, new HttpEntity<>(headers), CurrencyRateDto.class);

        assertEquals(HttpStatus.OK, currencyRateResponse.getStatusCode());
    }

    @Test
    @DirtiesContext
    void testLogout() {
        String token = registerUser();
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);

        ResponseEntity<Void> logoutResponse = restTemplate.exchange(
                "/api/v1/auth/logout",
                HttpMethod.POST,
                new HttpEntity<>(headers),
                Void.class
        );

        assertEquals(HttpStatus.OK, logoutResponse.getStatusCode());

        ResponseEntity<CurrencyRateDto> currencyRateResponse = restTemplate
                .exchange("/currencies/rates/USD", HttpMethod.GET, new HttpEntity<>(headers), CurrencyRateDto.class);

        assertEquals(HttpStatus.FORBIDDEN, currencyRateResponse.getStatusCode());
    }
}