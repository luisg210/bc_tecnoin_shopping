package com.luis.bc_tecnoin.payment_api.exception;


/**
 * Custom exception thrown when an order is not valid for payment.
 */
public class InvalidPaymentException extends RuntimeException {
    public InvalidPaymentException(Long orderId, String reason) {
        super("Payment for orderId " + orderId + " is invalid: " + reason);
    }
}