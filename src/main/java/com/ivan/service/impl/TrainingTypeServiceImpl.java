package com.ivan.service.impl;

import com.ivan.dao.TrainingTypeDao;
import com.ivan.exception.InvalidTrainingTypeException;
import com.ivan.model.TrainingType;
import com.ivan.service.TrainingTypeService;
import lombok.RequiredArgsConstructor;

import java.util.List;

/**
 * Service implementation for managing training types.
 * This class provides methods to retrieve, add, and delete training types.
 * It interacts with the database through {@link TrainingTypeDao} for data access.
 */
@RequiredArgsConstructor
public class TrainingTypeServiceImpl implements TrainingTypeService {

    private final TrainingTypeDao trainingTypeDao;

    /**
     * Retrieves all training types.
     *
     * @return a list of all training types
     */
    @Override
    public List<TrainingType> getAllTrainingTypes() {
        return trainingTypeDao.findAll();
    }

    /**
     * Adds a new training type.
     *
     * @param trainingType the training type to add
     */
    @Override
    public void addTrainingType(TrainingType trainingType) {
        trainingTypeDao.save(trainingType);
    }

    /**
     * Deletes a training type by its name.
     *
     * @param trainingTypeName the name of the training type to delete
     */
    @Override
    public void deleteTrainingType(String trainingTypeName) {
        trainingTypeDao.delete(getByTypeName(trainingTypeName));
    }

    /**
     * Retrieves a training type by its name.
     *
     * @param typeName the name of the training type to retrieve
     * @return the training type object
     * @throws InvalidTrainingTypeException if the training type is not found
     */
    @Override
    public TrainingType getByTypeName(String typeName) {
        return trainingTypeDao.findByTypeName(typeName)
                .orElseThrow(() -> new InvalidTrainingTypeException("Such type of training does not exist!"));
    }
}