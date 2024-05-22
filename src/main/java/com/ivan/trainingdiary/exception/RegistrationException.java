package com.ivan.trainingdiary.exception;

/**
 * Exception thrown to indicate a registration error.
 * This exception extends {@link RuntimeException}.
 *
 * @author sergeenkovv
 */
public class RegistrationException extends RuntimeException {

    /**
     * Constructs a RegistrationException with the specified detail message.
     *
     * @param message The detail message explaining the exception.
     */
    public RegistrationException(String message) {
        super(message);
    }
}