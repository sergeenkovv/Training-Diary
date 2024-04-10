package com.ivan.dao.impl;

import com.ivan.dao.TrainingTypeDao;
import com.ivan.model.TrainingType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MemoryTrainingTypeDaoImpl implements TrainingTypeDao {

    private final Map<Long, TrainingType> trainingTypeMap = new HashMap<>();
    private Long id = 1L;

    public MemoryTrainingTypeDaoImpl() {
        save(TrainingType.builder().typeName("CHEST").build());
        save(TrainingType.builder().typeName("BACK").build());
        save(TrainingType.builder().typeName("SHOULDERS").build());
        save(TrainingType.builder().typeName("LEGS").build());
    }

    @Override
    public List<TrainingType> findAll() {
        return List.copyOf(trainingTypeMap.values());
    }

    @Override
    public TrainingType save(TrainingType trainingType) {
        trainingType.setId(id++);
        trainingTypeMap.put(trainingType.getId(), trainingType);
        return trainingTypeMap.get(trainingType.getId());
    }
}