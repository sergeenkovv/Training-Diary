package com.ivan.trainingdiary.service.impl;


import com.ivan.loggingstarter.annotations.LoggableInfo;
import com.ivan.trainingdiary.repository.TrainingTypeRepository;
import com.ivan.trainingdiary.exception.DuplicateTrainingTypeException;
import com.ivan.trainingdiary.exception.InvalidTrainingTypeException;
import com.ivan.trainingdiary.model.TrainingType;
import com.ivan.trainingdiary.service.TrainingTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service implementation for managing training types.
 * This class provides methods to retrieve, add, and delete training types.
 * It interacts with the database through {@link TrainingTypeRepository} for data access.
 *
 * @author sergeenkovv
 */
@LoggableInfo(name = "class - TrainingTypeServiceImpl")
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TrainingTypeServiceImpl implements TrainingTypeService {

    private final TrainingTypeRepository trainingTypeDao;

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