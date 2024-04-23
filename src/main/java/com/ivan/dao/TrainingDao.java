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

    Optional<Training> findById(Long id);

    /**
     * Deletes a training from the data source.
     *
     * @param id The training`s id to delete.
     */
    boolean delete(Long id);

    void update(Training training);

    Optional<Training> findByAthleteIdAndTrainingDate(Long athleteId, LocalDate date);
}