package com.retail.rewards.model;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Represents a purchase transaction used to calculate reward points.
 *
 * @param id unique transaction identifier
 * @param customerId customer identifier
 * @param customerName customer display name
 * @param amount transaction amount in dollars
 * @param transactionDate date on which the transaction occurred
 */
public record Transaction(
        String id,
        String customerId,
        String customerName,
        BigDecimal amount,
        LocalDate transactionDate) {
}
