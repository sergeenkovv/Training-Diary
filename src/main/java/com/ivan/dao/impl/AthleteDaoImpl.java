package com.ivan.dao.impl;

import com.ivan.dao.AthleteDao;
import com.ivan.model.Athlete;
import com.ivan.model.Role;
import com.ivan.util.ConnectionManager;
import lombok.RequiredArgsConstructor;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class AthleteDaoImpl implements AthleteDao {

    private final ConnectionManager connectionProvider;

    @Override
    public Optional<Athlete> findByLogin(String login) {
        String sqlFindByLogin = """
                SELECT * FROM develop.athletes WHERE login = ?
                """;
        try (Connection connection = connectionProvider.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sqlFindByLogin)) {
            preparedStatement.setString(1, login);
            ResultSet resultSet = preparedStatement.executeQuery();

            return resultSet.next() ?
                    Optional.of(buildAthlete(resultSet))
                    : Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public Optional<Athlete> findById(Long id) {
        String sqlFindById = """
                SELECT * FROM develop.athletes WHERE id = ?
                """;
        try (Connection connection = connectionProvider.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sqlFindById)) {
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            return resultSet.next() ?
                    Optional.of(buildAthlete(resultSet))
                    : Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public List<Athlete> findAll() {
        String sqlFindAll = """
                SELECT * FROM develop.athletes;
                """;
        try (Connection connection = connectionProvider.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sqlFindAll)) {
            ResultSet resultSet = preparedStatement.executeQuery();

            List<Athlete> users = new ArrayList<>();
            while (resultSet.next()) {
                users.add(buildAthlete(resultSet));
            }

            return users;
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public Athlete save(Athlete athlete) {
        String sqlSave = """
                INSERT INTO develop.athletes (login, password, role)
                VALUES (?, ?, ?)
                """;
        try (Connection connection = connectionProvider.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sqlSave, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, athlete.getLogin());
            preparedStatement.setString(2, athlete.getPassword());
            preparedStatement.setString(3, athlete.getRole().toString());
            preparedStatement.executeUpdate();

            var generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                athlete.setId(generatedKeys.getLong("id"));
            }
            return athlete;
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private Athlete buildAthlete(ResultSet resultSet) throws SQLException {
        return Athlete.builder()
                .id(resultSet.getLong("id"))
                .login(resultSet.getString("login"))
                .password(resultSet.getString("password"))
                .role(Role.valueOf(resultSet.getString("role")))
                .build();
    }
}