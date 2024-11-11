package org.tbank.hw8.controller.impl;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
public class CurrencyControllerRatesIT {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testGetCurrencyRate_MissingCurrencyCode() throws Exception {
        mockMvc.perform(get("/currencies/rates/"))
                .andExpect(status().is(500))
                .andExpect(jsonPath("$.message").value("Unknown error: No static resource currencies/rates.")); // Примерное сообщение
    }

    @Test
    void testGetCurrencyRate_UnsupportedCurrency() throws Exception {
        mockMvc.perform(get("/currencies/rates/XYZ"))
                .andExpect(status().is(400))
                .andExpect(jsonPath("$.message").value("Currency code does not exist: XYZ"));
    }

    @Test
    void testGetCurrencyRate_CurrencyNotFoundByCb() throws Exception {
        mockMvc.perform(get("/currencies/rates/RUB"))
                .andExpect(status().is(404))
                .andExpect(jsonPath("$.message").value("Currency is not supported by Central Bank"));
    }
}