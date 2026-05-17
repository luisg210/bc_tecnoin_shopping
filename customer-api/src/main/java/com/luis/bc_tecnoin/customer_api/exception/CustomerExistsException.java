package com.luis.bc_tecnoin.customer_api.exception;


public class CustomerExistsException extends RuntimeException {
    public CustomerExistsException(String message) {
        super(message);
    }
}