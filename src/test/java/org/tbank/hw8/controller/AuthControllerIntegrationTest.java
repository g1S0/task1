package org.tbank.hw8.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
    private AuthService authService;

    @Autowired
    private MockMvc mockMvc;

    @BeforeAll
    public static void setUp() {
        System.setProperty("DB_URL", postgres.getJdbcUrl());
        System.setProperty("DB_USERNAME", postgres.getUsername());
        System.setProperty("DB_PASSWORD", postgres.getPassword());
    }

    private String registerUser() throws Exception {
        RegisterRequestDto registerRequest = new RegisterRequestDto();
        registerRequest.setFirstName("John");
        registerRequest.setSecondName("Doe");
        registerRequest.setEmail("john.doe@example.com");
        registerRequest.setPassword("Password@123)");

        String registerRequestJson = new ObjectMapper().writeValueAsString(registerRequest);
        MvcResult mvcResult = mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(registerRequestJson))
                .andExpect(status().isOk())
                .andReturn();

        String responseJson = mvcResult.getResponse().getContentAsString();
        AuthenticationResponseDto response = new ObjectMapper().readValue(responseJson, AuthenticationResponseDto.class);

        assertNotNull(response);
        return response.getToken();
    }

    private void resetPassword(String token, ChangePasswordRequestDto changePasswordRequest) throws Exception {
        String changePasswordRequestJson = new ObjectMapper().writeValueAsString(changePasswordRequest);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);

        mockMvc.perform(post("/api/v1/user/reset-password")
                        .headers(headers)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(changePasswordRequestJson))
                .andExpect(status().isOk());
    }

    @Test
    @DirtiesContext
    void testRegister() throws Exception {
        String token = registerUser();
        assertNotNull(token);
    }

    @Test
    @DirtiesContext
    void testRegisterAndGetCurrencyRate() throws Exception {
        String token = registerUser();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        MvcResult currencyRateResult = mockMvc.perform(get("/currencies/rates/USD")
                        .headers(headers))
                .andExpect(status().isOk())
                .andReturn();
        String responseJson = currencyRateResult.getResponse().getContentAsString();
        CurrencyRateDto currencyRateResponse = new ObjectMapper().readValue(responseJson, CurrencyRateDto.class);

        assertNotNull(currencyRateResponse);
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
    void testLogout() throws Exception {
        String token = registerUser();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);

        mockMvc.perform(post("/api/v1/auth/logout")
                        .headers(headers))
                .andExpect(status().isOk());

        mockMvc.perform(get("/currencies/rates/USD")
                        .headers(headers))
                .andExpect(status().isForbidden())
                .andReturn();
    }

    @Test
    @DirtiesContext
    void testConvertCurrencyAsAdmin() throws Exception {
        String adminToken = registerAdminUser();

        CurrencyConversionRequestDto conversionRequest = new CurrencyConversionRequestDto();
        conversionRequest.setFromCurrency("USD");
        conversionRequest.setToCurrency("EUR");
        conversionRequest.setAmount(100.0);

        String conversionRequestJson = new ObjectMapper().writeValueAsString(conversionRequest);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(adminToken);

        MvcResult conversionResult = mockMvc.perform(post("/currencies/convert")
                        .headers(headers)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(conversionRequestJson))
                .andExpect(status().isOk())
                .andReturn();

        String responseJson = conversionResult.getResponse().getContentAsString();
        ConvertedAmountDto conversionResponse = new ObjectMapper().readValue(responseJson, ConvertedAmountDto.class);

        assertNotNull(conversionResponse);
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
    void testConvertCurrencyAsUser() throws Exception {
        String userToken = registerRegularUser();

        CurrencyConversionRequestDto conversionRequest = new CurrencyConversionRequestDto();
        conversionRequest.setFromCurrency("USD");
        conversionRequest.setToCurrency("EUR");
        conversionRequest.setAmount(100.0);

        String conversionRequestJson = new ObjectMapper().writeValueAsString(conversionRequest);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(userToken);

        mockMvc.perform(post("/currencies/convert")
                        .headers(headers)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(conversionRequestJson))
                .andExpect(status().isForbidden())
                .andReturn();
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