package com.ivan.in;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * The ConsoleInputData class implements the InputData interface for reading data from the console input.
 * It provides methods for reading user input and closing the input stream.
 *
 * @author sergeenkovv
 */
public class ConsoleInputData implements InputData {

    private final BufferedReader reader;

    /**
     * Constructs a new ConsoleInputData object with a BufferedReader initialized with System.in.
     */
    public ConsoleInputData() {
        reader = new BufferedReader(new InputStreamReader(System.in));
    }

    /**
     * Reads user input from the console.
     *
     * @return A String containing the data read from the console.
     */
    @Override
    public String input() {
        String readData;
        try {
            readData = reader.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return readData;
    }

    /**
     * Closes the console input stream.
     */
    @Override
    public void closeInput() {
        try {
            reader.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}