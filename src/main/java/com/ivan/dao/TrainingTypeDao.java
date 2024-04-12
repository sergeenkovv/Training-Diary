package com.ivan.dao;

import com.ivan.model.TrainingType;

import java.util.List;
import java.util.Optional;

public interface TrainingTypeDao extends GeneralDao<Long, TrainingType> {

    List<TrainingType> findAll();

    Optional<TrainingType> findByTypeName(String typeName);

    void delete(TrainingType trainingToDelete);
}