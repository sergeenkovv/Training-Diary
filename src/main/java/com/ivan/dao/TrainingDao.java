package com.ivan.dao;

import com.ivan.model.Training;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Interface for accessing training data.
 * Extends {@link GeneralDao} with key type {@link Long} and entity type {@link Training}.
 */
public interface TrainingDao extends GeneralDao<Long, Training> {

    /**
     * Finds all trainings associated with a specific athlete ID.
     *
     * @param athleteId The ID of the athlete for which trainings are to be retrieved.
     * @return A {@link List} of trainings associated with the specified athlete ID.
     */
    List<Training> findAllByAthleteId(Long athleteId);

    /**
     * Finds a training by athlete ID and training date.
     *
     * @param athleteId The ID of the athlete.
     * @param date      The date of the training.
     * @return An {@link Optional} containing the training if found, otherwise an empty {@link Optional}.
     */
    Optional<Training> findByAthleteIdAndTrainingDate(Long athleteId, LocalDate date);

    /**
     * Deletes a training from the data source.
     *
     * @param training The training to delete.
     */
    void delete(Training training);
}