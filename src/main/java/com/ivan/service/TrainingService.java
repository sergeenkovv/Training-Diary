package com.ivan.service;

import com.ivan.model.Training;
import com.ivan.model.TrainingType;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;

public interface TrainingService {

    void addTraining(String trainingType, Integer setsAmount);

    Training editTraining();

//    Training getTrainingByDate();

    List<Training> getTrainingsSortedByDate();
}