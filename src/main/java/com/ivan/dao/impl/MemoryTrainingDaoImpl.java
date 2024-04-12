package com.ivan.dao.impl;

import com.ivan.dao.TrainingDao;
import com.ivan.model.Training;

import java.time.LocalDate;
import java.util.*;

public class MemoryTrainingDaoImpl implements TrainingDao {

    private final Map<Long, Training> trainingMap = new HashMap<>();
    private Long id = 1L;

    @Override
    public List<Training> findAllByAthleteId(Long athleteId) {
        List<Training> result = new ArrayList<>();
        for (Training training : trainingMap.values()) {
            if (training.getAthleteId().equals(athleteId)) {
                result.add(training);
            }
        }
        return result;
    }

    @Override
    public Optional<Training> findByAthleteIdAndTrainingDate(Long athleteId, LocalDate date) {
        Training training = null;
        List<Training> list = new ArrayList<>(trainingMap.values());

        for (Training tr : list) {
            if (tr.getAthleteId().equals(athleteId) && tr.getDate().equals(date)) {
                training = tr;
                break;
            }
        }
        if (training == null) return Optional.empty();
        return Optional.of(training);
    }

    @Override
    public void delete(Training training) {
        trainingMap.remove(training.getId());
    }

    @Override
    public Training save(Training training) {
        training.setId(getLastId());
        incrementId();
        trainingMap.put(training.getId(), training);
        return trainingMap.get(training.getId());
    }

    private Long getLastId() {
        return id;
    }

    private void incrementId() {
        id++;
    }
}