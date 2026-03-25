package com.retail.rewards.service;

import com.retail.rewards.exception.InvalidTransactionException;
import com.retail.rewards.model.Transaction;
import java.math.BigDecimal;
import java.math.RoundingMode;
import org.springframework.stereotype.Component;

/**
 * Calculates reward points for transactions.
 */
@Component
public class RewardCalculator {

    private static final BigDecimal FIFTY = BigDecimal.valueOf(50);
    private static final BigDecimal ONE_HUNDRED = BigDecimal.valueOf(100);

    /**
     * Calculates reward points for a transaction.
     *
     * @param transaction purchase transaction
     * @return reward points earned for the transaction
     */
    public long calculatePoints(Transaction transaction) {
        validateTransaction(transaction);
        BigDecimal amount = transaction.amount().setScale(0, RoundingMode.FLOOR);

        if (amount.compareTo(FIFTY) <= 0) {
            return 0L;
        }
        if (amount.compareTo(ONE_HUNDRED) <= 0) {
            return amount.subtract(FIFTY).longValue();
        }
        BigDecimal pointsAboveOneHundred = amount.subtract(ONE_HUNDRED).multiply(BigDecimal.valueOf(2));
        BigDecimal pointsBetweenFiftyAndOneHundred = ONE_HUNDRED.subtract(FIFTY);
        return pointsAboveOneHundred.add(pointsBetweenFiftyAndOneHundred).longValue();
    }

    private void validateTransaction(Transaction transaction) {
        if (transaction == null) {
            throw new InvalidTransactionException("Transaction must not be null.");
        }
        if (transaction.amount() == null) {
            throw new InvalidTransactionException("Transaction amount must not be null.");
        }
        if (transaction.amount().compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidTransactionException("Transaction amount must not be negative.");
        }
        if (transaction.transactionDate() == null) {
            throw new InvalidTransactionException("Transaction date must not be null.");
        }
        if (transaction.customerId() == null || transaction.customerId().isBlank()) {
            throw new InvalidTransactionException("Customer id must not be blank.");
        }
        if (transaction.customerName() == null || transaction.customerName().isBlank()) {
            throw new InvalidTransactionException("Customer name must not be blank.");
        }
    }
}
