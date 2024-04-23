package com.ivan.dao;

import com.ivan.model.TrainingType;

import java.util.List;
import java.util.Optional;

/**
 * Interface for accessing training type data.
 * Extends {@link GeneralDao} with key type {@link Long} and entity type {@link TrainingType}.
 */
public interface TrainingTypeDao extends GeneralDao<Long, TrainingType> {

    /**
     * Retrieves all training types stored in the data source.
     *
     * @return A {@link List} of all training types stored in the data source.
     */
    List<TrainingType> findAll();


    Optional<TrainingType> findById(Long id);

    /**
     * Deletes a training type from the data source.
     *
     * @param id The training type to delete.
     */
    boolean delete(Long id);
}