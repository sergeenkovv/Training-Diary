package com.ivan.dao;

import com.ivan.model.Athlete;

import java.util.Optional;

public interface AthleteDao extends GeneralDao<Long, Athlete> {

    Optional<Athlete> findByUsername(String username);
}