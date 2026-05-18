package com.luis.bc_tecnoin.payment_api.exception;


/**
 * Custom exception thrown when a payment is not found.
 */
public class PaymentNotFoundException extends RuntimeException {
    public PaymentNotFoundException(Long id) {
        super("Payment with id " + id + " not found");
    }
}