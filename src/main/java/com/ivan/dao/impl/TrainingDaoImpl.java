package com.ivan.dao.impl;

import com.ivan.dao.TrainingDao;
import com.ivan.model.Training;
import com.ivan.util.ConnectionManager;
import lombok.RequiredArgsConstructor;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class TrainingDaoImpl implements TrainingDao {

    private final ConnectionManager connectionProvider;

    @Override
    public List<Training> findAllByAthleteId(Long athleteId) {
        String sqlFindAllByAthleteId = """
                SELECT * FROM develop.trainings WHERE athlete_id = ?
                """;
        try (Connection connection = connectionProvider.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sqlFindAllByAthleteId)) {
            preparedStatement.setLong(1, athleteId);
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Training> result = new ArrayList<>();
            while (resultSet.next()) {
                result.add(buildTraining(resultSet));
            }
            return result;
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public Optional<Training> findById(Long athleteId) {
        String sqlFindById = """
                SELECT * FROM develop.trainings WHERE id = ?
                """;
        try (Connection connection = connectionProvider.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sqlFindById)) {
            preparedStatement.setLong(1, athleteId);
            ResultSet resultSet = preparedStatement.executeQuery();

            return resultSet.next() ?
                    Optional.of(buildTraining(resultSet))
                    : Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public Optional<Training> findByAthleteIdAndTrainingDate(Long athleteId, LocalDate date) {
        String sqlFindById = """
                SELECT * FROM develop.trainings WHERE athlete_id = ? AND date = ?
                """;
        try (Connection connection = connectionProvider.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sqlFindById)) {
            preparedStatement.setLong(1, athleteId);
            preparedStatement.setDate(2, Date.valueOf(date));
            ResultSet resultSet = preparedStatement.executeQuery();

            return resultSet.next() ?
                    Optional.of(buildTraining(resultSet))
                    : Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public boolean delete(Long id) {
        String sqlDelete = """
                DELETE FROM develop.trainings WHERE id = ?
                """;
        try (Connection connection = connectionProvider.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sqlDelete)) {
            preparedStatement.setLong(1, id);

            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void update(Training training) {
        String sqlUpdate = """
                UPDATE develop.trainings
                SET sets_amount = ?,
                    type_id = ?
                WHERE id = ?
                """;
        try (Connection connection = connectionProvider.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sqlUpdate)) {
            preparedStatement.setInt(1, training.getSetsAmount());
            preparedStatement.setLong(2, training.getTypeId());
            preparedStatement.setLong(3, training.getAthleteId());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public Training save(Training training) {
        String sqlSave = """
                INSERT INTO develop.trainings (sets_amount, date, type_id, athlete_id) VALUES (?, ?, ?, ?)
                """;
        try (Connection connection = connectionProvider.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sqlSave, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setInt(1, training.getSetsAmount());
            preparedStatement.setDate(2, Date.valueOf(training.getDate()));
            preparedStatement.setLong(3, training.getTypeId());
            preparedStatement.setLong(4, training.getAthleteId());
            preparedStatement.executeUpdate();

            var generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                training.setId(generatedKeys.getLong("id"));
            }
            return training;
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private Training buildTraining(ResultSet resultSet) throws SQLException {
        return Training.builder()
                .id(resultSet.getLong("id"))
                .setsAmount(resultSet.getInt("sets_amount"))
                .date(resultSet.getDate("date").toLocalDate())
                .typeId(resultSet.getLong("type_id"))
                .athleteId(resultSet.getLong("athlete_id"))
                .build();
    }
}