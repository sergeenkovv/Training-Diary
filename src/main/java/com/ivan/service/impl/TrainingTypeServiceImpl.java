package com.ivan.service.impl;

import com.ivan.dao.TrainingTypeDao;
import com.ivan.exception.InvalidTrainingTypeException;
import com.ivan.exception.TrainingLimitExceededException;
import com.ivan.model.TrainingType;
import com.ivan.service.TrainingTypeService;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class TrainingTypeServiceImpl implements TrainingTypeService {

    private final TrainingTypeDao trainingTypeDao;

    @Override
    public List<TrainingType> showAvailableTrainingTypes() {
        return trainingTypeDao.findAll();
    }

    @Override
    public TrainingType save(TrainingType trainingType) {
        return trainingTypeDao.save(trainingType);
    }

    @Override
    public TrainingType getByTypeName(String typeName) {
        return trainingTypeDao.findByTypeName(typeName)
                .orElseThrow(() -> new InvalidTrainingTypeException("Such training does not exist!"));
    }
}