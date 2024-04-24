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
 * The LiquibaseMigration class demonstrates how to use Liquibase for database migrations.
 * This class provides methods for running migrations and obtaining the singleton instance of LiquibaseMigration.
 *
 * @author sergeenkovv
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LiquibaseMigration {

    private static final LiquibaseMigration LIQUIBASE_MIGRATION = new LiquibaseMigration();

    private static final String SQL_CREATE_SCHEMA = "CREATE SCHEMA IF NOT EXISTS migration";

    /**
     * Runs database migrations using Liquibase.
     *
     * @param connection the Connection object to the database
     * @throws RuntimeException if an error occurs during migration
     */
    public void runMigrations(Connection connection) {
        try (Connection conn = connection;
             PreparedStatement preparedStatement = conn.prepareStatement(SQL_CREATE_SCHEMA)) {
            preparedStatement.executeUpdate();

            Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(conn));
            database.setLiquibaseSchemaName("migration");

            Liquibase liquibase = new Liquibase("db/changelog/changelog.xml", new ClassLoaderResourceAccessor(), database);
            liquibase.update();

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * Returns the singleton instance of LiquibaseMigration.
     *
     * @return the singleton instance of LiquibaseMigration
     */
    public static LiquibaseMigration getInstance() {
        return LIQUIBASE_MIGRATION;
    }
}