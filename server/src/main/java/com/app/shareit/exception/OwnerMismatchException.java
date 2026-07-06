package com.app.shareit.exception;

public class OwnerMismatchException extends RuntimeException {
    public OwnerMismatchException(String message) {
        super(message);
    }
}
