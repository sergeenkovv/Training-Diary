package com.ivan.trainingdiary.exception;

/**
 * Exception thrown when an invalid training type is encountered.
 * This exception extends {@link RuntimeException}.
 *
 * @author sergeenkovv
 */
public class InvalidTrainingTypeException extends RuntimeException {

    /**
     * Constructs an InvalidTrainingTypeException with the specified detail message.
     *
     * @param message The detail message explaining the exception.
     */
    public InvalidTrainingTypeException(String message) {
        super(message);
    }
}