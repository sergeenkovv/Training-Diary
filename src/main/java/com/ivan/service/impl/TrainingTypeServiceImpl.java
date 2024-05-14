package com.ivan.service.impl;

import com.ivan.annotations.Loggable;
import com.ivan.dao.TrainingTypeDao;
import com.ivan.exception.DuplicateTrainingTypeException;
import com.ivan.exception.InvalidTrainingTypeException;
import com.ivan.model.TrainingType;
import com.ivan.service.TrainingTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service implementation for managing training types.
 * This class provides methods to retrieve, add, and delete training types.
 * It interacts with the database through {@link TrainingTypeDao} for data access.
 *
 * @author sergeenkovv
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
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
     * @param typeName the training type to add
     */

    @Override
    @Loggable
    @Transactional
    public void addTrainingType(String typeName) {
        Optional<TrainingType> byTypeName = trainingTypeDao.findByTypeName(typeName);

        if (byTypeName.isPresent()) {
            throw new DuplicateTrainingTypeException("This type of training already exists!");
        }

        TrainingType type = TrainingType.builder()
                .typeName(typeName)
                .build();

        trainingTypeDao.save(type);
    }

    /**
     * Deletes a training type by it's name.
     *
     * @param typeName the name of the training type to delete
     */
    @Override
    @Loggable
    @Transactional
    public void deleteTrainingType(String typeName) {
        trainingTypeDao.delete(getByTypeName(typeName).getId());
    }

    /**
     * Retrieves a training type by it's name.
     *
     * @param id the id of the training type to retrieve
     * @return the training type object
     * @throws InvalidTrainingTypeException if the training type is not found
     */
    @Override
    public TrainingType getById(Long id) {
        return trainingTypeDao.findById(id)
                .orElseThrow(() -> new InvalidTrainingTypeException("Such type of training does not exist!"));
    }

    /**
     * Retrieves a training type by it's name.
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