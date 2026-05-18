package com.luis.bc_tecnoin.detail_api.exception;


/**
 * Custom exception thrown when an order is not valid for detail creation.
 */
public class ProductNotFoundException extends RuntimeException {
    public ProductNotFoundException(Long productId) {
        super("Product with id " + productId + " is not valid for detail creation");
    }
}