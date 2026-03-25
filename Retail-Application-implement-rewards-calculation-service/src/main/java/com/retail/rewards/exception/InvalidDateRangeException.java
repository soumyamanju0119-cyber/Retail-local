package com.retail.rewards.exception;

/**
 * Thrown when an API consumer supplies an invalid date range.
 */
public class InvalidDateRangeException extends RuntimeException {

    /**
     * Creates an exception with a validation message.
     *
     * @param message validation details
     */
    public InvalidDateRangeException(String message) {
        super(message);
    }
}
