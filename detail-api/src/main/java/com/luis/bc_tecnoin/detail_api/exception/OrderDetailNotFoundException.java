package com.luis.bc_tecnoin.detail_api.exception;


/**
 * Custom exception thrown when an order detail is not found.
 */
public class OrderDetailNotFoundException extends RuntimeException {
    public OrderDetailNotFoundException(Long id) {
        super("Order detail with id " + id + " not found");
    }
}