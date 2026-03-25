package com.retail.rewards.exception;

/**
 * Thrown when a requested customer does not exist in the transaction dataset.
 */
public class CustomerNotFoundException extends RuntimeException {

    /**
     * Creates an exception for a missing customer.
     *
     * @param customerId missing customer identifier
     */
    public CustomerNotFoundException(String customerId) {
        super("Customer not found for id: " + customerId);
    }
}
