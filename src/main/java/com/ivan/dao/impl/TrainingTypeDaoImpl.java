package com.ivan.dao.impl;

import com.ivan.dao.TrainingTypeDao;
import com.ivan.model.TrainingType;
import com.ivan.util.ConnectionManager;
import lombok.RequiredArgsConstructor;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * DAO implementation for {@link TrainingTypeDao} interface.
 *
 * @author sergeenkovv
 */
@RequiredArgsConstructor
public class TrainingTypeDaoImpl implements TrainingTypeDao {

    private final ConnectionManager connectionProvider;

    /**
     * Retrieves all training types from the database.
     *
     * @return a list of all training types
     */
    @Override
    public List<TrainingType> findAll() {
        String sqlFindAll = """
                SELECT * FROM develop.training_types
                """;
        try (Connection connection = connectionProvider.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sqlFindAll)) {
            ResultSet resultSet = preparedStatement.executeQuery();

            List<TrainingType> trainingType = new ArrayList<>();
            while (resultSet.next()) {
                trainingType.add(buildTrainingType(resultSet));
            }

            return trainingType;
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * Searches for a training type by id.
     *
     * @param id the training type's id
     * @return an optional training type with the given id if found, or empty optional otherwise
     */
    @Override
    public Optional<TrainingType> findById(Long id) {
        String sqlFindById = """
                SELECT * FROM develop.training_types WHERE id = ?
                """;
        try (Connection connection = connectionProvider.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sqlFindById)) {
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            TrainingType trainingType = null;
            if (resultSet.next()) {
                trainingType = buildTrainingType(resultSet);
            }
            return Optional.ofNullable(trainingType);
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * Searches for a training type by type name.
     *
     * @param typeName the training type's name
     * @return an optional training type with the given type name if found, or empty optional otherwise
     */
    @Override
    public Optional<TrainingType> findByTypeName(String typeName) {
        String sqlFindByTypeName = """
                SELECT * FROM develop.training_types WHERE type_name = ?
                """;
        try (Connection connection = connectionProvider.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sqlFindByTypeName)) {
            preparedStatement.setString(1, typeName);
            ResultSet resultSet = preparedStatement.executeQuery();

            return resultSet.next() ?
                    Optional.of(buildTrainingType(resultSet))
                    : Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * Deletes a training type by its id.
     *
     * @param id the training type's id
     * @return {@code true} if the training type was deleted successfully, {@code false} otherwise
     */
    @Override
    public boolean delete(Long id) {
        String sqlDelete = """
                DELETE FROM develop.training_types WHERE id = ?
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
     * Saves a training type to the database.
     *
     * @param trainingType the training type to save
     * @return the saved training type with the generated id
     */
    @Override
    public TrainingType save(TrainingType trainingType) {
        String sqlSave = """
                INSERT INTO develop.training_types (type_name)
                VALUES (?)
                """;
        try (Connection connection = connectionProvider.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sqlSave, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, trainingType.getTypeName());
            preparedStatement.executeUpdate();

            var generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                trainingType.setId(generatedKeys.getLong("id"));
            }
            return trainingType;
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * Builds a {@link TrainingType} instance from a {@link ResultSet}.
     *
     * @param resultSet the result set to build the training type instance from
     * @return the built training type instance
     * @throws SQLException if there is an error reading from the result set
     */
    private TrainingType buildTrainingType(ResultSet resultSet) throws SQLException {
        return TrainingType.builder()
                .id(resultSet.getLong("id"))
                .typeName(resultSet.getString("type_name"))
                .build();
    }
}