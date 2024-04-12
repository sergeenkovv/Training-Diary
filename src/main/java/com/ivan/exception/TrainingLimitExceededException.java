package com.ivan.exception;

public class TrainingLimitExceededException extends RuntimeException {

    public TrainingLimitExceededException(String message) {
        super(message);
    }
}