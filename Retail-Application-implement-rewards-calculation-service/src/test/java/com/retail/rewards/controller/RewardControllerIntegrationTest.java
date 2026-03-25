package com.retail.rewards.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Integration tests for the rewards REST endpoints.
 */
@SpringBootTest
@AutoConfigureMockMvc
class RewardControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    /**
     * Verifies the API returns summaries for all customers in the demo dataset.
     *
     * @throws Exception when the request fails unexpectedly
     */
    @Test
    void shouldReturnRewardsForAllCustomers() throws Exception {
        mockMvc.perform(get("/api/rewards"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.startDate").value("2026-01-08"))
                .andExpect(jsonPath("$.endDate").value("2026-03-17"))
                .andExpect(jsonPath("$.customers.length()").value(3))
                .andExpect(jsonPath("$.customers[0].customerId").value("C001"))
                .andExpect(jsonPath("$.customers[0].monthlyRewards[0].month").value("2026-01"))
                .andExpect(jsonPath("$.customers[0].monthlyRewards[0].points").value(94))
                .andExpect(jsonPath("$.customers[0].totalPoints").value(243));
    }

    /**
     * Verifies the API can filter the response to a single customer and date range.
     *
     * @throws Exception when the request fails unexpectedly
     */
    @Test
    void shouldReturnRewardsForSingleCustomerWithinDateRange() throws Exception {
        mockMvc.perform(get("/api/rewards/C002")
                        .param("startDate", "2026-02-01")
                        .param("endDate", "2026-03-31"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerId").value("C002"))
                .andExpect(jsonPath("$.monthlyRewards.length()").value(2))
                .andExpect(jsonPath("$.monthlyRewards[0].month").value("2026-02"))
                .andExpect(jsonPath("$.monthlyRewards[0].points").value(315))
                .andExpect(jsonPath("$.monthlyRewards[1].month").value("2026-03"))
                .andExpect(jsonPath("$.monthlyRewards[1].points").value(54))
                .andExpect(jsonPath("$.totalPoints").value(369));
    }

    /**
     * Verifies unknown customers produce a 404 response.
     *
     * @throws Exception when the request fails unexpectedly
     */
    @Test
    void shouldReturnNotFoundForUnknownCustomer() throws Exception {
        mockMvc.perform(get("/api/rewards/C999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Customer not found for id: C999"));
    }

    /**
     * Verifies invalid date ranges produce a 400 response.
     *
     * @throws Exception when the request fails unexpectedly
     */
    @Test
    void shouldReturnBadRequestForInvalidDateRange() throws Exception {
        mockMvc.perform(get("/api/rewards").param("startDate", "2026-03-31").param("endDate", "2026-01-01"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Start date must be on or before end date."));
    }

    /**
     * Verifies invalid request parameter values produce a 400 response.
     *
     * @throws Exception when the request fails unexpectedly
     */
    @Test
    void shouldReturnBadRequestForInvalidDateParameter() throws Exception {
        mockMvc.perform(get("/api/rewards").param("startDate", "bad-date"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Invalid value for parameter 'startDate'."));
    }
}
