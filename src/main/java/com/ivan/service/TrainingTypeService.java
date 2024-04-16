package com.ivan.service;

import com.ivan.model.TrainingType;

import java.util.List;

/**
 * Interface for managing training types.
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
     * Deletes a training type by its name.
     *
     * @param trainingTypeName The name of the training type to be deleted.
     */
    void deleteTrainingType(String trainingTypeName);

    /**
     * Retrieves a training type by its name.
     *
     * @param typeName The name of the training type.
     */
    TrainingType getByTypeName(String typeName);
}