package com.ivan.service;

import com.ivan.model.Training;

import java.time.LocalDate;
import java.util.List;

/**
 * Interface for managing athlete training data.
 */
public interface TrainingService {

    /**
     * Adds a new training for the specified athlete.
     *
     * @param athleteId    The ID of the athlete.
     * @param trainingType The type of training.
     * @param setsAmount   The number of sets.
     */
    void addTraining(Long athleteId, Long trainingType, Integer setsAmount);

    /**
     * Edits an existing training for the specified athlete.
     *
     * @param athleteId    The ID of the athlete.
     * @param date         The date of the training.
     * @param trainingType The type of training.
     * @param setsAmount   The number of sets.
     */
    void editTraining(Long athleteId, LocalDate date, Long trainingType, String setsAmount);

    /**
     * Retrieves a list of trainings for the specified athlete sorted by date.
     *
     * @param athleteId The ID of the athlete.
     * @return A sorted list of trainings.
     */
    List<Training> getTrainingsSortedByDate(Long athleteId);

    /**
     * Retrieves a list of trainings for the specified athlete sorted by sets amount.
     *
     * @param athleteId The ID of the athlete.
     */
    List<Training> getTrainingsSortedBySetsAmount(Long athleteId);

    /**
     * Deletes a training for the specified athlete on the given date.
     *
     * @param athleteId The ID of the athlete.
     * @param date      The date of the training to be deleted.
     */
    void deleteTraining(Long athleteId, LocalDate date);

    /**
     * Retrieves a training for the specified athlete on the given date.
     *
     * @param athleteId The ID of the athlete.
     * @param date      The date of the training.
     */
    Training getTrainingByAthleteIdAndDate(Long athleteId, LocalDate date);
}