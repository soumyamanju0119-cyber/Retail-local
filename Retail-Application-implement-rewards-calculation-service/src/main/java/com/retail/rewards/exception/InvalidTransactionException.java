package com.retail.rewards.exception;

/**
 * Thrown when a transaction contains invalid values.
 */
public class InvalidTransactionException extends RuntimeException {

    /**
     * Creates an exception with a validation message.
     *
     * @param message validation details
     */
    public InvalidTransactionException(String message) {
        super(message);
    }
}
