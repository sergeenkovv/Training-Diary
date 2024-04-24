package com.ivan.dao.impl;

import com.ivan.dao.AuditDao;
import com.ivan.model.ActionType;
import com.ivan.model.Audit;
import com.ivan.util.ConnectionManager;
import lombok.RequiredArgsConstructor;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO implementation for {@link AuditDao} interface.
 *
 * @author sergeenkovv
 */
@RequiredArgsConstructor
public class AuditDaoImpl implements AuditDao {

    private final ConnectionManager connectionProvider;

    /**
     * Retrieves all audits for a given athlete login.
     *
     * @param athleteLogin the athlete's login
     * @return a list of audits for the given athlete login
     */
    @Override
    public List<Audit> findAllByLogin(String athleteLogin) {
        String sqlFindAllByAthleteLogin = """
                SELECT * FROM develop.audit WHERE athlete_login = ?
                """;
        try (Connection connection = connectionProvider.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sqlFindAllByAthleteLogin)) {
            preparedStatement.setString(1, athleteLogin);
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Audit> result = new ArrayList<>();
            while (resultSet.next()) {
                result.add(buildAudit(resultSet));
            }
            return result;
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * Saves an audit to the database.
     *
     * @param audit the audit to save
     * @return the saved audit with the generated id
     */
    @Override
    public Audit save(Audit audit) {
        String sqlSave = """
                INSERT INTO develop.audit (athlete_login, action_type, date)
                VALUES (?,?,?);
                """;
        try (Connection connection = connectionProvider.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sqlSave, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, audit.getAthleteLogin());
            preparedStatement.setString(2, audit.getActionType().toString());
            preparedStatement.setDate(3, Date.valueOf(audit.getDate()));
            preparedStatement.executeUpdate();

            var generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                audit.setId(generatedKeys.getLong("id"));
            }
            return audit;
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * Builds an {@link Audit} instance from a {@link ResultSet}.
     *
     * @param resultSet the result set to build the audit from
     * @return the built audit instance
     * @throws SQLException if there is an error reading from the result set
     */
    private Audit buildAudit(ResultSet resultSet) throws SQLException {
        return Audit.builder()
                .id(resultSet.getLong("id"))
                .athleteLogin(resultSet.getString("athlete_login"))
                .actionType(ActionType.valueOf(resultSet.getString("action_type")))
                .date(resultSet.getDate("date").toLocalDate())
                .build();
    }
}