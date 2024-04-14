package com.ivan.dao;

import com.ivan.model.Training;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TrainingDao extends GeneralDao<Long, Training> {

    List<Training> findAllByAthleteId(Long athleteId);

    Optional<Training> findByAthleteIdAndTrainingDate(Long athleteId, LocalDate date);

    void delete(Training training);
}