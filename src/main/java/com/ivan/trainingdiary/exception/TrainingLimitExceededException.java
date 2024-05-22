package com.ivan.trainingdiary.exception;

/**
 * Exception thrown when the training limit is exceeded.
 * This exception extends {@link RuntimeException}.
 *
 * @author sergeenkovv
 */
public class TrainingLimitExceededException extends RuntimeException {

    /**
     * Constructs a TrainingLimitExceededException with the specified detail message.
     *
     * @param message The detail message explaining the exception.
     */
    public TrainingLimitExceededException(String message) {
        super(message);
    }
}