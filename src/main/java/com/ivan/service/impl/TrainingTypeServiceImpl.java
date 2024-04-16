package com.ivan.service.impl;

import com.ivan.dao.TrainingTypeDao;
import com.ivan.exception.InvalidTrainingTypeException;
import com.ivan.model.TrainingType;
import com.ivan.service.TrainingTypeService;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class TrainingTypeServiceImpl implements TrainingTypeService {

    private final TrainingTypeDao trainingTypeDao;

    @Override
    public List<TrainingType> getAllTrainingTypes() {
        return trainingTypeDao.findAll();
    }

    @Override
    public void addTrainingType(TrainingType trainingType) {
        trainingTypeDao.save(trainingType);
    }

    @Override
    public void deleteTrainingType(String trainingTypeName) {
        trainingTypeDao.delete(getByTypeName(trainingTypeName));
    }

    @Override
    public TrainingType getByTypeName(String typeName) {
        return trainingTypeDao.findByTypeName(typeName)
                .orElseThrow(() -> new InvalidTrainingTypeException("Such type of training does not exist!"));
    }
}