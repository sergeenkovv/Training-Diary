package com.ivan.exception;

public class InvalidTrainingTypeException extends RuntimeException {

    public InvalidTrainingTypeException(String message) {
        super(message);
    }
}