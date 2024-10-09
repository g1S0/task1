package org.tbank.hw8.controller.impl;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class CurrencyControllerConvertIT {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testGetCurrencyRate_UnsupportedCurrency() throws Exception {
        String invalidRequest = "{"
                + "\"fromCurrency\": \"XYZ\","
                + "\"toCurrency\": \"RUB\","
                + "\"amount\": 10"
                + "}";

        mockMvc.perform(post("/currencies/convert")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidRequest))
                .andExpect(status().is(400))
                .andExpect(jsonPath("$.message").value("Currency code does not exist: XYZ"));
    }

    @Test
    void testGetCurrencyRate_CurrencyNotFoundByCb() throws Exception {
        String invalidRequest = "{"
                + "\"fromCurrency\": \"USD\","
                + "\"toCurrency\": \"RUB\","
                + "\"amount\": 10"
                + "}";

        mockMvc.perform(post("/currencies/convert")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidRequest))
                .andExpect(status().is(404))
                .andExpect(jsonPath("$.message").value("Currency is not supported by Central Bank"));
    }

    @Test
    void testConvertCurrency_InvalidAmount() throws Exception {
        String invalidRequest = "{"
                + "\"fromCurrency\": \"USD\","
                + "\"toCurrency\": \"RUB\","
                + "\"amount\": 0"
                + "}";

        mockMvc.perform(post("/currencies/convert")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidRequest))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Amount must be greater than 0"));
    }

    @Test
    void testConvertCurrency_MissingFromCurrency() throws Exception {
        String requestBody = "{"
                + "\"toCurrency\": \"RUB\","
                + "\"amount\": 100.0"
                + "}";

        mockMvc.perform(post("/currencies/convert")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("fromCurrency is required"));
    }

    @Test
    void testConvertCurrency_MissingToCurrency() throws Exception {
        String requestBody = "{"
                + "\"fromCurrency\": \"USD\","
                + "\"amount\": 100.0"
                + "}";

        mockMvc.perform(post("/currencies/convert")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("toCurrency is required"));
    }

    @Test
    void testConvertCurrency_MissingAmount() throws Exception {
        String requestBody = "{"
                + "\"fromCurrency\": \"USD\","
                + "\"toCurrency\": \"RUB\""
                + "}";

        mockMvc.perform(post("/currencies/convert")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("amount is required"));
    }
}
