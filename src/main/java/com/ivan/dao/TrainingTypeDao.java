package com.ivan.dao;

import com.ivan.model.TrainingType;

import java.util.Optional;

public interface TrainingTypeDao extends GeneralDao<Long, TrainingType>{

    Optional<TrainingType> findByTypeName(String typeName);

}