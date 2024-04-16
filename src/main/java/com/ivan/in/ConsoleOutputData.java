package com.ivan.in;

/**
 * The ConsoleOutputData class implements the OutputData interface for displaying output on the console.
 * It provides methods for printing data to the standard output and standard error streams.
 */
public class ConsoleOutputData implements OutputData {

    /**
     * Prints the data object to the standard output stream.
     *
     * @param data The data object to be displayed.
     */
    @Override
    public void output(Object data) {
        System.out.println(data.toString());
    }

    /**
     * Prints the data object to the standard error stream.
     *
     * @param data The data object to be displayed as an error message.
     */
    @Override
    public void errOutput(Object data) {
        System.err.println(data.toString());
    }
}