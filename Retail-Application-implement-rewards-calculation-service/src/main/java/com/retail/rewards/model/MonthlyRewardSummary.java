package com.retail.rewards.model;

/**
 * Represents a monthly reward total for a customer.
 *
 * @param month month in yyyy-MM format
 * @param points reward points earned in the month
 */
public record MonthlyRewardSummary(String month, long points) {
}
