package com.ivan.liquibase;

import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;
import lombok.AllArgsConstructor;

import java.sql.Connection;
import java.sql.PreparedStatement;

/**
 * The LiquibaseMigration class demonstrates how to use Liquibase for database migrations.
 * This class provides methods for running migrations and obtaining the singleton instance of LiquibaseMigration.
 *
 * @author sergeenkovv
 */
@AllArgsConstructor
public class LiquibaseMigration {

    private static final String SQL_CREATE_SCHEMA = "CREATE SCHEMA IF NOT EXISTS migration";

    private final Connection connection;
    private final String changeLogFile;
    private final String schemaName;

    /**
     * Performs database migration.
     */
    public void runMigrations() {
        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_CREATE_SCHEMA)) {
            preparedStatement.executeUpdate();

            Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(connection));
            database.setLiquibaseSchemaName(schemaName);

            Liquibase liquibase = new Liquibase(changeLogFile, new ClassLoaderResourceAccessor(), database);
            liquibase.update();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}