package com.retail.rewards.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.retail.rewards.exception.InvalidTransactionException;
import com.retail.rewards.model.Transaction;
import java.math.BigDecimal;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link RewardCalculator}.
 */
class RewardCalculatorTest {

    private RewardCalculator rewardCalculator;

    @BeforeEach
    void setUp() {
        rewardCalculator = new RewardCalculator();
    }

    /**
     * Verifies rewards are calculated across threshold ranges.
     */
    @Test
    void shouldCalculateRewardPointsAcrossThresholds() {
        assertEquals(0L, rewardCalculator.calculatePoints(transaction("49.99")));
        assertEquals(0L, rewardCalculator.calculatePoints(transaction("50.00")));
        assertEquals(1L, rewardCalculator.calculatePoints(transaction("51.00")));
        assertEquals(50L, rewardCalculator.calculatePoints(transaction("100.00")));
        assertEquals(90L, rewardCalculator.calculatePoints(transaction("120.00")));
        assertEquals(92L, rewardCalculator.calculatePoints(transaction("121.75")));
    }

    /**
     * Verifies invalid transaction amounts are rejected.
     */
    @Test
    void shouldRejectNegativeAmounts() {
        Transaction transaction = new Transaction("T-1", "C001", "Alice", new BigDecimal("-1.00"), LocalDate.now());

        assertThrows(InvalidTransactionException.class, () -> rewardCalculator.calculatePoints(transaction));
    }

    /**
     * Verifies missing transaction data is rejected.
     */
    @Test
    void shouldRejectNullTransaction() {
        assertThrows(InvalidTransactionException.class, () -> rewardCalculator.calculatePoints(null));
    }

    private Transaction transaction(String amount) {
        return new Transaction("T-1", "C001", "Alice", new BigDecimal(amount), LocalDate.of(2026, 1, 1));
    }
}
