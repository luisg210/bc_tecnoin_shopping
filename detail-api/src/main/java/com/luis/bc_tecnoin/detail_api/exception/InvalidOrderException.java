package com.luis.bc_tecnoin.detail_api.exception;


/**
 * Custom exception thrown when an order is not valid for detail creation.
 */
public class InvalidOrderException extends RuntimeException {
    public InvalidOrderException(Long orderId) {
        super("Order with id " + orderId + " is not valid for detail creation");
    }
}