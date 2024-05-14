package com.ivan.dao.impl;

import com.ivan.dao.AthleteDao;
import com.ivan.dao.TrainingDao;
import com.ivan.dao.TrainingTypeDao;
import com.ivan.model.Training;
import com.ivan.util.ConnectionManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * DAO implementation for {@link TrainingDao} interface.
 *
 * @author sergeenkovv
 */
@Repository
@RequiredArgsConstructor
public class TrainingDaoImpl implements TrainingDao {

    private final ConnectionManager connectionProvider;
    private final TrainingTypeDao trainingTypeDao;
    private final AthleteDao athleteDao;

    /**
     * Retrieves all training sessions for a given athlete id.
     *
     * @param athleteId the athlete's id
     * @return a list of training sessions for the given athlete id
     */
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

    /**
     * Searches for a training session by id.
     *
     * @param id the training session's id
     * @return an optional training session with the given id if found, or empty optional otherwise
     */
    @Override
    public Optional<Training> findById(Long id) {
        String sqlFindById = """
                SELECT * FROM develop.trainings WHERE id = ?
                """;
        try (Connection connection = connectionProvider.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sqlFindById)) {
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            return resultSet.next() ?
                    Optional.of(buildTraining(resultSet))
                    : Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * Searches for a training session by athlete id and training date.
     *
     * @param athleteId the athlete's id
     * @param date      the training date
     * @return an optional training session with the given athlete id and training date if found, or empty optional otherwise
     */
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

    /**
     * Deletes a training session by its id.
     *
     * @param id the training session's id
     * @return {@code true} if the training session was deleted successfully, {@code false} otherwise
     */
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

    /**
     * Updates a training session.
     *
     * @param training the training session to update
     */
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
            preparedStatement.setLong(2, training.getTrainingType().getId());
            preparedStatement.setLong(3, training.getAthlete().getId());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * Saves a training session to the database.
     *
     * @param training the training session to save
     * @return the saved training session with the generated id
     */
    @Override
    public Training save(Training training) {
        String sqlSave = """
                INSERT INTO develop.trainings (sets_amount, date, type_id, athlete_id) VALUES (?, ?, ?, ?)
                """;
        try (Connection connection = connectionProvider.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sqlSave, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setInt(1, training.getSetsAmount());
            preparedStatement.setDate(2, Date.valueOf(training.getDate()));
            preparedStatement.setLong(3, training.getTrainingType().getId());
            preparedStatement.setLong(4, training.getAthlete().getId());
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

    /**
     * Builds a {@link Training} instance from a {@link ResultSet}.
     *
     * @param resultSet the result set to build the training instance from
     * @return the built training instance
     * @throws SQLException if there is an error reading from the result set
     */
    private Training buildTraining(ResultSet resultSet) throws SQLException {
        return Training.builder()
                .id(resultSet.getLong("id"))
                .setsAmount(resultSet.getInt("sets_amount"))
                .date(resultSet.getDate("date").toLocalDate())
                .trainingType(trainingTypeDao.findById(resultSet.getLong("type_id")).orElse(null))
                .athlete(athleteDao.findById(resultSet.getLong("athlete_id")).orElse(null))
                .build();
    }
}