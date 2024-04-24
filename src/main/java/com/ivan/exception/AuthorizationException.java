package com.ivan.exception;

/**
 * Exception thrown to indicate that authorization has failed.
 * This exception extends {@link RuntimeException}.
 *
 * @author sergeenkovv
 */
public class AuthorizationException extends RuntimeException {

    /**
     * Constructs an AuthorizationException with the specified detail message.
     *
     * @param message The detail message explaining the exception.
     */
    public AuthorizationException(String message) {
        super(message);
    }
}