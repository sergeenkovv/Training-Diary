package com.ivan.trainingdiary.repository;

import com.ivan.trainingdiary.model.Training;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Interface for accessing training data.
 * Extends {@link GeneralRepository} with key type {@link Long} and entity type {@link Training}.
 *
 * @author sergeenkovv
 */
public interface TrainingRepository extends GeneralRepository<Long, Training> {

    /**
     * Finds all trainings associated with a specific athlete ID.
     *
     * @param athleteId The ID of the athlete for which trainings are to be retrieved.
     * @return A {@link List} of trainings associated with the specified athlete ID.
     */
    List<Training> findAllByAthleteId(Long athleteId);

    /**
     * Finds a training by ID.
     *
     * @param id The ID of the training to find.
     * @return An {@link Optional} containing the training if found, otherwise an empty {@link Optional}.
     */
    Optional<Training> findById(Long id);

    /**
     * Deletes a training from the data source.
     *
     * @param id The training's id to delete.
     */
    boolean delete(Long id);

    /**
     * Updates old workout
     *
     * @param training The training to update
     */
    void update(Training training);

    /**
     * Finds a training by athlete's ID and training date .
     *
     * @param athleteId The athlete's ID.
     * @param date      The training date.
     * @return An {@link Optional} containing the training if found, otherwise an empty {@link Optional}.
     */
    Optional<Training> findByAthleteIdAndTrainingDate(Long athleteId, LocalDate date);
}