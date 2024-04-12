package com.ivan.service;

import com.ivan.model.TrainingType;

import java.util.List;

public interface TrainingTypeService {

    List<TrainingType> getAvailableTrainingTypes();

    void save (TrainingType trainingType);

    TrainingType getByTypeName(String typeName);
}