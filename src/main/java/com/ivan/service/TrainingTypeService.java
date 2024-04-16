package com.ivan.service;

import com.ivan.model.TrainingType;

import java.util.List;

public interface TrainingTypeService {

    List<TrainingType> getAllTrainingTypes();

    void addTrainingType(TrainingType trainingType);

    void deleteTrainingType(String trainingTypeName);

    TrainingType getByTypeName(String typeName);

}