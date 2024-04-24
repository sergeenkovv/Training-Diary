package com.ivan.dao;

import com.ivan.model.TrainingType;

import java.util.List;
import java.util.Optional;

/**
 * Interface for accessing training type data.
 * Extends {@link GeneralDao} with key type {@link Long} and entity type {@link TrainingType}.
 *
 * @author sergeenkovv
 */
public interface TrainingTypeDao extends GeneralDao<Long, TrainingType> {

    /**
     * Retrieves all training types stored in the data source.
     *
     * @return A {@link List} of all training types stored in the data source.
     */
    List<TrainingType> findAll();

    /**
     * Finds a training type by ID.
     *
     * @param id The ID of the training type to find.
     * @return An {@link Optional} containing the training type if found, otherwise an empty {@link Optional}.
     */
    Optional<TrainingType> findById(Long id);

    /**
     * Finds an athlete by their login.
     *
     * @param typeName The login of the athlete to find.
     * @return An {@link Optional} containing the athlete if found, otherwise an empty {@link Optional}.
     */
    Optional<TrainingType> findByTypeName(String typeName);

    /**
     * Deletes a training type from the data source.
     *
     * @param id The training type to delete.
     */
    boolean delete(Long id);
}