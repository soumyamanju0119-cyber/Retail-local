package com.retail.rewards.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.retail.rewards.exception.CustomerNotFoundException;
import com.retail.rewards.exception.InvalidDateRangeException;
import com.retail.rewards.exception.InvalidTransactionException;
import com.retail.rewards.model.CustomerRewardSummary;
import com.retail.rewards.model.RewardResponse;
import com.retail.rewards.model.Transaction;
import com.retail.rewards.repository.TransactionRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link RewardService}.
 */
class RewardServiceTest {

    /**
     * Verifies rewards are grouped by customer and by dynamic month values.
     */
    @Test
    void shouldAggregateRewardsByCustomerAndMonth() {
        RewardService rewardService = new RewardService(repositoryWithValidData(), new RewardCalculator());

        RewardResponse response = rewardService.getRewards(null, null);

        assertEquals(LocalDate.of(2026, 1, 10), response.startDate());
        assertEquals(LocalDate.of(2026, 3, 5), response.endDate());
        assertEquals(2, response.customers().size());

        CustomerRewardSummary firstCustomer = response.customers().get(0);
        assertEquals("C001", firstCustomer.customerId());
        assertEquals(2, firstCustomer.monthlyRewards().size());
        assertEquals("2026-01", firstCustomer.monthlyRewards().get(0).month());
        assertEquals(90L, firstCustomer.monthlyRewards().get(0).points());
        assertEquals(65L, firstCustomer.monthlyRewards().get(1).points());
        assertEquals(155L, firstCustomer.totalPoints());

        CustomerRewardSummary secondCustomer = response.customers().get(1);
        assertEquals("C002", secondCustomer.customerId());
        assertEquals(2, secondCustomer.monthlyRewards().size());
        assertEquals(0L, secondCustomer.monthlyRewards().get(0).points());
        assertEquals(75L, secondCustomer.monthlyRewards().get(1).points());
        assertEquals(75L, secondCustomer.totalPoints());
    }

    /**
     * Verifies date filters restrict the dataset.
     */
    @Test
    void shouldFilterRewardsByDateRange() {
        RewardService rewardService = new RewardService(repositoryWithValidData(), new RewardCalculator());

        CustomerRewardSummary summary = rewardService.getRewardsByCustomer(
                "C001", LocalDate.of(2026, 2, 1), LocalDate.of(2026, 2, 28));

        assertEquals("C001", summary.customerId());
        assertEquals(1, summary.monthlyRewards().size());
        assertEquals("2026-02", summary.monthlyRewards().get(0).month());
        assertEquals(65L, summary.totalPoints());
    }

    /**
     * Verifies unknown customers are rejected.
     */
    @Test
    void shouldThrowWhenCustomerDoesNotExist() {
        RewardService rewardService = new RewardService(repositoryWithValidData(), new RewardCalculator());

        assertThrows(CustomerNotFoundException.class,
                () -> rewardService.getRewardsByCustomer("C999", null, null));
    }

    /**
     * Verifies invalid date ranges are rejected.
     */
    @Test
    void shouldThrowWhenDateRangeIsInvalid() {
        RewardService rewardService = new RewardService(repositoryWithValidData(), new RewardCalculator());

        assertThrows(InvalidDateRangeException.class,
                () -> rewardService.getRewards(LocalDate.of(2026, 3, 1), LocalDate.of(2026, 1, 1)));
    }

    /**
     * Verifies invalid transactions surface as validation failures.
     */
    @Test
    void shouldThrowWhenRepositoryReturnsInvalidTransaction() {
        TransactionRepository repository = () -> List.of(new Transaction(
                "T-99", "C001", "Alice", new BigDecimal("-5.00"), LocalDate.of(2026, 1, 1)));
        RewardService rewardService = new RewardService(repository, new RewardCalculator());

        assertThrows(InvalidTransactionException.class, () -> rewardService.getRewards(null, null));
    }

    private TransactionRepository repositoryWithValidData() {
        return () -> List.of(
                new Transaction("T-1", "C001", "Alice", new BigDecimal("120.00"), LocalDate.of(2026, 1, 10)),
                new Transaction("T-2", "C001", "Alice", new BigDecimal("110.00"), LocalDate.of(2026, 2, 5)),
                new Transaction("T-3", "C001", "Alice", new BigDecimal("55.00"), LocalDate.of(2026, 2, 7)),
                new Transaction("T-4", "C002", "Bob", new BigDecimal("20.00"), LocalDate.of(2026, 1, 11)),
                new Transaction("T-5", "C002", "Bob", new BigDecimal("125.00"), LocalDate.of(2026, 3, 5)));
    }
}
