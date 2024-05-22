package com.ivan.trainingdiary.repository.impl;

import com.ivan.trainingdiary.repository.AthleteRepository;
import com.ivan.trainingdiary.model.Athlete;
import com.ivan.trainingdiary.model.Role;
import com.ivan.trainingdiary.util.ConnectionManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * DAO implementation for {@link AthleteRepository} interface.
 *
 * @author sergeenkovv
 */
@Repository
@RequiredArgsConstructor
public class AthleteRepositoryImpl implements AthleteRepository {

    private final ConnectionManager connectionProvider;

    /**
     * Searches for an athlete by login.
     *
     * @param login the athlete's login
     * @return an optional athlete with the given login if found, or empty optional otherwise
     */
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

    /**
     * Searches for an athlete by id.
     *
     * @param id the athlete's id
     * @return an optional athlete with the given id if found, or empty optional otherwise
     */
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

    /**
     * Retrieves all athletes from the database.
     *
     * @return a list of all athletes
     */
    @Override
    public List<Athlete> findAll() {
        String sqlFindAll = """
                SELECT * FROM develop.athletes;
                """;
        try (Connection connection = connectionProvider.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sqlFindAll)) {
            ResultSet resultSet = preparedStatement.executeQuery();

            List<Athlete> athletes = new ArrayList<>();
            while (resultSet.next()) {
                athletes.add(buildAthlete(resultSet));
            }

            return athletes;
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * Saves an athlete to the database.
     *
     * @param athlete the athlete to save
     * @return the saved athlete with the generated id
     */
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
            preparedStatement.setString(3, athlete.getRole().getAuthority());
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

    /**
     * Builds an {@link Athlete} instance from a {@link ResultSet}.
     *
     * @param resultSet the result set to build the athlete from
     * @return the built athlete instance
     * @throws SQLException if there is an error reading from the result set
     */
    private Athlete buildAthlete(ResultSet resultSet) throws SQLException {
        return Athlete.builder()
                .id(resultSet.getLong("id"))
                .login(resultSet.getString("login"))
                .password(resultSet.getString("password"))
                .role(Role.valueOf(resultSet.getString("role")))
                .build();
    }
}