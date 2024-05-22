package com.ivan.trainingdiary.util;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Manages database connections.
 */
@Component
@Getter
public class ConnectionManager {

    @Value("${spring.datasource.url}")
    private String url;
    @Value("${spring.datasource.driver-class-name}")
    private String driver;
    @Value("${spring.datasource.username}")
    private String username;
    @Value("${spring.datasource.password}")
    private String password;

    /**
     * Gets a connection to the database.
     *
     * @return the connection
     * @throws RuntimeException if a connection cannot be established
     */
    public Connection getConnection() {
        try {
            Class.forName(getDriver());

            return DriverManager.getConnection(
                    getUrl(),
                    getUsername(),
                    getPassword()
            );
        } catch (SQLException e) {
            throw new RuntimeException("Failed to get a database connection.", e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}