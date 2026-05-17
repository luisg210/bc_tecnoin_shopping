package com.luis.bc_tecnoin.customer_api.exception;


public class CustomerNotExistsException extends RuntimeException {
    public CustomerNotExistsException(String message) {
        super(message);
    }
}