package com.ivan.service;

import com.ivan.model.TrainingType;

import java.util.List;

public interface TrainingTypeService {

    List<TrainingType> showAvailableTrainingTypes();

    TrainingType save (TrainingType trainingType);

    TrainingType getByTypeName(String typeName);
}