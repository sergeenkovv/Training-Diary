package com.ivan.service.impl;

import com.ivan.dao.TrainingTypeDao;
import com.ivan.model.TrainingType;
import com.ivan.service.TrainingTypeService;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class TrainingTypeServiceImpl implements TrainingTypeService {

    private final TrainingTypeDao trainingTypeDao;

    @Override
    public List<TrainingType> showAvailableTrainingTypes() {
//        return trainingTypeDao.
        return null;
    }

    @Override
    public TrainingType save(TrainingType trainingType) {
        return null;
    }
}
