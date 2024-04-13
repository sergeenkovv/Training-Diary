package com.ivan.dao.impl;

import com.ivan.dao.TrainingDao;
import com.ivan.model.Training;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class MemoryTrainingDaoImpl implements TrainingDao {

    private final Map<Long, Training> trainingMap = new HashMap<>();
    private Long id = 1L;

    @Override
    public List<Training> findAllByAthleteId(Long athleteId) {
        return trainingMap.values().stream()
                .filter(training -> training.getAthleteId().equals(athleteId))
                .collect(Collectors.toList());
    }

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