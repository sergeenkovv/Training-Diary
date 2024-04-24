package com.ivan.exception;

/**
 * Exception thrown when an invalid argument is passed to a method or constructor.
 * This exception extends {@link RuntimeException}.
 *
 * @author sergeenkovv
 */
public class InvalidArgumentException extends RuntimeException {

    /**
     * Constructs an InvalidArgumentException with the specified detail message.
     *
     * @param message The detail message explaining the exception.
     */
    public InvalidArgumentException(String message) {
        super(message);
    }
}