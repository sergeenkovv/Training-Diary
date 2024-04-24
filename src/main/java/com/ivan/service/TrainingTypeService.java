package com.ivan.service;

import com.ivan.model.TrainingType;

import java.util.List;

/**
 * Interface for managing training types.
 *
 * @author sergeenkovv
 */
public interface TrainingTypeService {

    /**
     * Retrieves a list of all available training types.
     */
    List<TrainingType> getAllTrainingTypes();

    /**
     * Adds a new training type.
     *
     * @param trainingType The training type to be added.
     */
    void addTrainingType(TrainingType trainingType);

    /**
     * Deletes a training type by it's name.
     *
     * @param typeName The name of the training type to be deleted.
     */
    void deleteTrainingType(String typeName);

    /**
     * Retrieves a training type by it's id.
     *
     * @param id The id of the training type.
     */
    TrainingType getById(Long id);

    /**
     * Retrieves a training type by it's name.
     *
     * @param typeName The id of the training type.
     */
    TrainingType getByTypeName(String typeName);
}