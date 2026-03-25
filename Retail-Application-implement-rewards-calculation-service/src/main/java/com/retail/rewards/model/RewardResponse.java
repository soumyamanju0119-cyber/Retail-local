package com.retail.rewards.model;

import java.time.LocalDate;
import java.util.List;

/**
 * Represents the API response for reward summaries.
 *
 * @param startDate inclusive start date of the evaluated period
 * @param endDate inclusive end date of the evaluated period
 * @param customers reward summaries for each customer
 */
public record RewardResponse(
        LocalDate startDate,
        LocalDate endDate,
        List<CustomerRewardSummary> customers) {
}
