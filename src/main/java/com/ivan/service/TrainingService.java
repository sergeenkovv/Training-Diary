package com.ivan.service;

import com.ivan.model.Training;
import com.ivan.model.TrainingType;

import java.time.LocalDate;
import java.util.List;

public interface TrainingService {

    void addTraining(Long athleteId, String trainingType, Integer setsAmount, LocalDate date);

    Training updateTraining(Long athleteId, LocalDate date, String trainingType, String setsAmount);

//    Training getTrainingByDate();

    List<Training> getTrainingsSortedByDate(Long athleteId);

    List<Training> getTrainingsBySetsAmount(Long athleteId);

    void deleteTraining(Long athleteId, LocalDate date);
}