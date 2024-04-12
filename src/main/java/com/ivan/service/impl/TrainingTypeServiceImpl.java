package com.ivan.service.impl;

import com.ivan.dao.TrainingTypeDao;
import com.ivan.exception.InvalidTrainingTypeException;
import com.ivan.model.TrainingType;
import com.ivan.service.TrainingTypeService;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class TrainingTypeServiceImpl implements TrainingTypeService {

    private final TrainingTypeDao trainingTypeDao;

    @Override
    public List<TrainingType> getAvailableTrainingTypes() {
        return trainingTypeDao.findAll();
    }

    @Override
    public void save(TrainingType trainingType) {
        trainingTypeDao.save(trainingType);
    }

    public void delete(String trainingTypeName) {
        Optional<TrainingType> optionalTrainingType = trainingTypeDao.findByTypeName(trainingTypeName);

        if (optionalTrainingType.isEmpty()) {
            throw new InvalidTrainingTypeException("Such type of training does not exist!");
        }

        TrainingType trainingToDelete = optionalTrainingType.get();
        trainingTypeDao.delete(trainingToDelete);
    }

    @Override
    public TrainingType getByTypeName(String typeName) {
        return trainingTypeDao.findByTypeName(typeName)
                .orElseThrow(() -> new InvalidTrainingTypeException("Such type of training does not exist!"));
    }
}