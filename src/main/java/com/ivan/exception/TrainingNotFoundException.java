package com.ivan.exception;

/**
 * Exception thrown when a training is not found.
 * This exception extends {@link RuntimeException}.
 *
 * @author sergeenkovv
 */
public class TrainingNotFoundException extends RuntimeException {

    /**
     * Constructs a TrainingNotFoundException with the specified detail message.
     *
     * @param message The detail message explaining the exception.
     */
    public TrainingNotFoundException(String message) {
        super(message);
    }
}