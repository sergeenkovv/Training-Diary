package com.ivan.dao.impl;

import com.ivan.dao.TrainingTypeDao;
import com.ivan.model.TrainingType;

import java.util.*;

/**
 * Implementation of the {@link TrainingTypeDao} interface that stores training types in memory.
 * This implementation uses a HashMap to store training types with their corresponding IDs.
 */
public class MemoryTrainingTypeDaoImpl implements TrainingTypeDao {

    private final Map<Long, TrainingType> trainingTypeMap = new HashMap<>();
    private Long id = 1L;

    /**
     * Constructs a new MemoryTrainingTypeDaoImpl instance and initializes it with predefined training types.
     */
    public MemoryTrainingTypeDaoImpl() {
        save(TrainingType.builder().typeName("CHEST").build());
        save(TrainingType.builder().typeName("BACK").build());
        save(TrainingType.builder().typeName("SHOULDERS").build());
        save(TrainingType.builder().typeName("LEGS").build());
    }

    /**
     * Retrieves all training types stored in memory.
     *
     * @return A {@link List} of all training types stored in memory.
     */
    @Override
    public List<TrainingType> findAll() {
        return List.copyOf(trainingTypeMap.values());
    }

    /**
     * Retrieves a training type by its name.
     *
     * @param typeName The name of the training type to find.
     * @return An {@link Optional} containing the training type if found, otherwise an empty {@link Optional}.
     */
    @Override
    public Optional<TrainingType> findByTypeName(String typeName) {
        TrainingType trainingType;
        List<TrainingType> list = new ArrayList<>(trainingTypeMap.values());

        trainingType = list.stream()
                .filter(type -> type.getTypeName().equals(typeName))
                .findFirst()
                .orElse(null);
        return trainingType == null ? Optional.empty() : Optional.of(trainingType);
    }

    /**
     * Deletes a training type from the memory storage.
     *
     * @param trainingToDelete The training type to delete.
     */
    @Override
    public void delete(TrainingType trainingToDelete) {
        trainingTypeMap.remove(trainingToDelete.getId());
    }

    /**
     * Saves a training type to the memory storage.
     *
     * @param trainingType The training type to save.
     * @return The saved training type.
     */
    @Override
    public TrainingType save(TrainingType trainingType) {
        trainingType.setId(getLastId());
        incrementId();
        trainingTypeMap.put(trainingType.getId(), trainingType);
        return trainingTypeMap.get(trainingType.getId());
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
     * Increments the ID for the next training type.
     */
    private void incrementId() {
        id++;
    }
}