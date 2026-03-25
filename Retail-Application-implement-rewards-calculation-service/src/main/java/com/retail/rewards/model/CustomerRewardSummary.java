package com.retail.rewards.model;

import java.util.List;

/**
 * Represents total and monthly reward points for a customer.
 *
 * @param customerId customer identifier
 * @param customerName customer display name
 * @param monthlyRewards monthly breakdown of reward points
 * @param totalPoints total reward points across the selected period
 */
public record CustomerRewardSummary(
        String customerId,
        String customerName,
        List<MonthlyRewardSummary> monthlyRewards,
        long totalPoints) {
}
