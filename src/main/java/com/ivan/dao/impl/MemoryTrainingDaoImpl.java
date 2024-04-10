package com.ivan.dao.impl;

import com.ivan.dao.TrainingDao;
import com.ivan.model.Training;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MemoryTrainingDaoImpl implements TrainingDao {

    private final Map<Long, Training> trainingMap = new HashMap<>();
    private Long id = 1L;

    @Override
    public List<Training> findAllByOrderByDateAsc() {
        return null;
    }

    @Override
    public List<Training> findAll() {
        return List.copyOf(trainingMap.values());
    }

    @Override
    public Training save(Training training) {
        training.setId(id++);
        trainingMap.put(training.getId(), training);
        return trainingMap.get(training.getId());
    }
}
