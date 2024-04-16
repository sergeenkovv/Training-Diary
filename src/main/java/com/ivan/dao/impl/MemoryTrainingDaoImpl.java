package com.ivan.dao.impl;

import com.ivan.dao.TrainingDao;
import com.ivan.model.Training;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Implementation of the {@link TrainingDao} interface that stores training data in memory.
 * This implementation uses a HashMap to store trainings with their corresponding IDs.
 */
public class MemoryTrainingDaoImpl implements TrainingDao {

    private final Map<Long, Training> trainingMap = new HashMap<>();
    private Long id = 1L;

    /**
     * Retrieves all trainings associated with a specific athlete ID.
     *
     * @param athleteId The ID of the athlete for which trainings are to be retrieved.
     * @return A {@link List} of trainings associated with the specified athlete ID.
     */
    @Override
    public List<Training> findAllByAthleteId(Long athleteId) {
        return trainingMap.values().stream()
                .filter(training -> training.getAthleteId().equals(athleteId))
                .collect(Collectors.toList());
    }

    /**
     * Retrieves a training by athlete ID and training date.
     *
     * @param athleteId The ID of the athlete.
     * @param date      The date of the training.
     * @return An {@link Optional} containing the training if found, otherwise an empty {@link Optional}.
     */
    @Override
    public Optional<Training> findByAthleteIdAndTrainingDate(Long athleteId, LocalDate date) {
        Training training;
        List<Training> list = new ArrayList<>(trainingMap.values());

        training = list.stream()
                .filter(tr -> tr.getAthleteId().equals(athleteId) && tr.getDate().equals(date))
                .findFirst().orElse(null);
        if (training == null) return Optional.empty();
        return Optional.of(training);
    }

    /**
     * Deletes a training from the memory storage.
     *
     * @param training The training to delete.
     */
    @Override
    public void delete(Training training) {
        trainingMap.remove(training.getId());
    }

    /**
     * Saves a training to the memory storage.
     *
     * @param training The training to save.
     * @return The saved training.
     */
    @Override
    public Training save(Training training) {
        training.setId(getLastId());
        incrementId();
        trainingMap.put(training.getId(), training);
        return trainingMap.get(training.getId());
    }

    /**
     * Retrieves the last assigned ID.
     *
     * @return The last assigned ID.
     */
    private Long getLastId() {
        return id;
    }

    /**
     * Increments the ID for the next training.
     */
    private void incrementId() {
        id++;
    }
}