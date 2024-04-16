package com.ivan.exception;

/**
 * Exception thrown to indicate a registration error.
 * This exception extends {@link RuntimeException}.
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