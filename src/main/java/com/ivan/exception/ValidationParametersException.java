package com.ivan.exception;

public class ValidationParametersException extends RuntimeException {
    public ValidationParametersException(String message) {
        super(message);
    }
}