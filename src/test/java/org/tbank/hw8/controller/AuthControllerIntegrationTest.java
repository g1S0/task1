package org.tbank.hw8.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.tbank.hw8.dto.*;
import org.tbank.hw8.entity.Role;
import org.tbank.hw8.entity.User;
import org.tbank.hw8.service.AuthService;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Testcontainers
public class AuthControllerIntegrationTest {

    @Container
    public static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15")
            .withDatabaseName("admin")
            .withUsername("admin")
            .withPassword("user_data");

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private AuthService authService;

    @Autowired
    private MockMvc mockMvc;

    @BeforeAll
    public static void setUp() {
        System.setProperty("DB_URL", postgres.getJdbcUrl());
        System.setProperty("DB_USERNAME", postgres.getUsername());
        System.setProperty("DB_PASSWORD", postgres.getPassword());
    }

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
    void testResetPassword() throws Exception {
        String token = registerUser();

        ChangePasswordRequestDto changePasswordRequest = new ChangePasswordRequestDto();
        changePasswordRequest.setCode("0000");
        changePasswordRequest.setCurrentPassword("Password@123)");
        changePasswordRequest.setNewPassword("NewPassword@123)");
        changePasswordRequest.setConfirmationPassword("NewPassword@123)");

        resetPassword(token, changePasswordRequest);
        MvcResult oldTokenResult = authenticate("john.doe@example.com", "Password@123)");
        assertEquals(HttpStatus.UNAUTHORIZED.value(), oldTokenResult.getResponse().getStatus());

        MvcResult newAuthResult = authenticate("john.doe@example.com", "NewPassword@123)");
        assertEquals(HttpStatus.OK.value(), newAuthResult.getResponse().getStatus());

        String newAuthResponseBody = newAuthResult.getResponse().getContentAsString();
        AuthenticationResponseDto newAuthResponse = new ObjectMapper().readValue(newAuthResponseBody, AuthenticationResponseDto.class);
        assertNotNull(newAuthResponse);
        String newToken = newAuthResponse.getToken();
        assertNotNull(newToken);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(newToken);

        MvcResult currencyRateResult = mockMvc.perform(get("/currencies/rates/USD")
                        .headers(headers))
                .andExpect(status().isOk())
                .andReturn();

        String currencyRateResponseBody = currencyRateResult.getResponse().getContentAsString();
        CurrencyRateDto currencyRateResponse = new ObjectMapper().readValue(currencyRateResponseBody, CurrencyRateDto.class);

        assertEquals(HttpStatus.OK.value(), currencyRateResult.getResponse().getStatus());
        assertNotNull(currencyRateResponse);
    }

    private MvcResult authenticate(String email, String password) throws Exception {
        AuthenticationRequestDto authRequest = new AuthenticationRequestDto();
        authRequest.setEmail(email);
        authRequest.setPassword(password);

        String authRequestJson = new ObjectMapper().writeValueAsString(authRequest);

        return mockMvc.perform(post("/api/v1/auth/authenticate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(authRequestJson))
                .andReturn();
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

    @Test
    @DirtiesContext
    void testConvertCurrencyAsAdmin() {
        String adminToken = registerAdminUser();

        CurrencyConversionRequestDto conversionRequest = new CurrencyConversionRequestDto();
        conversionRequest.setFromCurrency("USD");
        conversionRequest.setToCurrency("EUR");
        conversionRequest.setAmount(100.0);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(adminToken);

        ResponseEntity<ConvertedAmountDto> conversionResponse = restTemplate
                .exchange("/currencies/convert", HttpMethod.POST, new HttpEntity<>(conversionRequest, headers), ConvertedAmountDto.class);

        assertEquals(HttpStatus.OK, conversionResponse.getStatusCode());
        assertNotNull(conversionResponse.getBody());
    }

    private String registerAdminUser() {
        User user = new User();
        user.setFirstName("Admin");
        user.setSecondName("User");
        user.setEmail("admin@example.com");
        user.setPassword("Admin@123)");
        user.setRole(Role.ADMIN);

        return authService.register(user).getToken();
    }

    @Test
    @DirtiesContext
    void testConvertCurrencyAsUser() {
        String userToken = registerRegularUser();

        CurrencyConversionRequestDto conversionRequest = new CurrencyConversionRequestDto();
        conversionRequest.setFromCurrency("USD");
        conversionRequest.setToCurrency("EUR");
        conversionRequest.setAmount(100.0);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(userToken);

        ResponseEntity<ConvertedAmountDto> conversionResponse = restTemplate
                .exchange("/currencies/convert", HttpMethod.POST, new HttpEntity<>(conversionRequest, headers), ConvertedAmountDto.class);

        assertEquals(HttpStatus.FORBIDDEN, conversionResponse.getStatusCode());
    }

    private String registerRegularUser() {
        User user = new User();
        user.setFirstName("Regular");
        user.setSecondName("User");
        user.setEmail("user@example.com");
        user.setPassword("User@123)");
        user.setRole(Role.USER);

        return authService.register(user).getToken();
    }
}