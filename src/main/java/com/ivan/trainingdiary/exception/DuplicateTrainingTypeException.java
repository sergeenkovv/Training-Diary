package com.ivan.trainingdiary.exception;

/**
 * This exception is thrown when a duplicate training type is encountered.
 * This exception extends {@link RuntimeException}.
 *
 * @author sergeenkovv
 */
public class DuplicateTrainingTypeException extends RuntimeException {

    public DuplicateTrainingTypeException(String message) {
        super(message);
    }
}
