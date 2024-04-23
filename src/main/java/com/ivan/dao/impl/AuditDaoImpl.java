package com.ivan.dao.impl;

import com.ivan.dao.AuditDao;
import com.ivan.model.ActionType;
import com.ivan.model.Audit;
import com.ivan.util.ConnectionManager;
import lombok.RequiredArgsConstructor;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class AuditDaoImpl implements AuditDao {

    private final ConnectionManager connectionProvider;

    @Override
    public List<Audit> findAllByLogin(String login) {
        String sqlFindAllByAthleteLogin = """
                SELECT * FROM develop.audit WHERE athlete_login = ?
                """;
        try (Connection connection = connectionProvider.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sqlFindAllByAthleteLogin)) {
            preparedStatement.setString(1, login);
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

    private Audit buildAudit(ResultSet resultSet) throws SQLException {
        return Audit.builder()
                .id(resultSet.getLong("id"))
                .athleteLogin(resultSet.getString("athlete_login"))
                .actionType(ActionType.valueOf(resultSet.getString("action_type")))
                .date(resultSet.getDate("date").toLocalDate())
                .build();
    }
}