package com.ivan.service;

import com.ivan.model.Training;

import java.time.LocalDate;
import java.util.List;

public interface TrainingService {

    void addTraining(Long athleteId, String trainingType, Integer setsAmount);

    void editTraining(Long athleteId, LocalDate date, String trainingType, String setsAmount);

    List<Training> getTrainingsSortedByDate(Long athleteId);

    List<Training> getTrainingsSortedBySetsAmount(Long athleteId);

    void deleteTraining(Long athleteId, LocalDate date);

    Training getTrainingByAthleteIdAndDate(Long athleteId, LocalDate date);
}