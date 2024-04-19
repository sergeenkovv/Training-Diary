package com.ivan.liquibase;

import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.sql.Connection;
import java.sql.PreparedStatement;

/**
 * The LiquibaseDemo class demonstrates how to use Liquibase for database migrations.
 * This class provides methods for running migrations and obtaining the singleton instance of LiquibaseDemo.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LiquibaseDemo {

    private static final LiquibaseDemo liquibaseDemo = new LiquibaseDemo();

    private static final String SQL_CREATE_SCHEMA = "CREATE SCHEMA IF NOT EXISTS migration";

    /**
     * Runs database migrations using Liquibase.
     *
     * @param connection the Connection object to the database
     * @throws RuntimeException if an error occurs during migration
     */
    public void runMigrations(Connection connection) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_CREATE_SCHEMA)) {
            preparedStatement.executeUpdate();
            Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(connection));
            database.setLiquibaseSchemaName("migration");

            Liquibase liquibase = new Liquibase("db/changelog/changelog.xml", new ClassLoaderResourceAccessor(), database);
            liquibase.update();

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * Returns the singleton instance of LiquibaseDemo.
     *
     * @return the singleton instance of LiquibaseDemo
     */
    public static LiquibaseDemo getInstance() {
        return liquibaseDemo;
    }
}