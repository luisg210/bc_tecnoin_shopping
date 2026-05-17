package com.luis.bc_tecnoin.order_api.exception;


/**
 * Custom exception thrown when a customer is not found.
 */
public class CustomerNotFoundException extends RuntimeException {
    public CustomerNotFoundException(Long userId) {
        super("Customer with userId " + userId + " not found");
    }
}