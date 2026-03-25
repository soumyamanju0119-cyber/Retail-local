package com.retail.rewards.model;

import java.time.Instant;

/**
 * Represents a consistent error payload returned by the REST API.
 *
 * @param timestamp time when the error occurred
 * @param status HTTP status code
 * @param error HTTP reason phrase
 * @param message business or validation error details
 */
public record ErrorResponse(Instant timestamp, int status, String error, String message) {
}
