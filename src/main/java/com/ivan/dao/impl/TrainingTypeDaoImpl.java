package com.ivan.dao.impl;

import com.ivan.dao.TrainingTypeDao;
import com.ivan.model.TrainingType;
import com.ivan.util.ConnectionManager;
import lombok.RequiredArgsConstructor;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class TrainingTypeDaoImpl implements TrainingTypeDao {

    private final ConnectionManager connectionProvider;

    @Override
    public List<TrainingType> findAll() {
        String sqlFindAll = """
                SELECT * FROM develop.training_types
                """;
        try (Connection connection = connectionProvider.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sqlFindAll)) {
            ResultSet resultSet = preparedStatement.executeQuery();

            List<TrainingType> meterTypes = new ArrayList<>();
            while (resultSet.next()) {
                meterTypes.add(buildTrainingType(resultSet));
            }

            return meterTypes;
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

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

    private TrainingType buildTrainingType(ResultSet resultSet) throws SQLException {
        return TrainingType.builder()
                .id(resultSet.getLong("id"))
                .typeName(resultSet.getString("type_name"))
                .build();
    }
}