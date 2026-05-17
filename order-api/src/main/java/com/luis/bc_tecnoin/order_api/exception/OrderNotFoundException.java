package com.luis.bc_tecnoin.order_api.exception;


/**
 * Custom exception thrown when an order is not found.
 */
public class OrderNotFoundException extends RuntimeException {
    public OrderNotFoundException(Long id) {
        super("Order with id " + id + " not found");
    }
}