package com.ivan.exception;

/**
 * Exception thrown when an athlete is not found.
 * This exception extends {@link RuntimeException}.
 *
 * @author sergeenkovv
 */
public class AthleteNotFoundException extends RuntimeException {

    /**
     * Constructs an AthleteNotFoundException with the specified detail message.
     *
     * @param message The detail message explaining the exception.
     */
    public AthleteNotFoundException(String message) {
        super(message);
    }
}