package com.ivan.util;

import lombok.AllArgsConstructor;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * Manages database connections using the provided URL, username, and password.
 * This class facilitates the establishment of database connections by encapsulating connection details.
 */
@AllArgsConstructor
public class ConnectionManager {

    /**
     * The URL used to establish database connections.
     */
    private final String url;
    /**
     * The username used for authentication when establishing database connections.
     */
    private final String username;
    /**
     * The password used for authentication when establishing database connections.
     */
    private final String password;

    /**
     * Retrieves a database connection using the stored connection details.
     *
     * @return a {@link java.sql.Connection} object representing the established database connection
     * @throws RuntimeException if an error occurs while establishing the connection
     */
    public Connection getConnection() {
        try {
            return DriverManager.getConnection(url, username, password);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}