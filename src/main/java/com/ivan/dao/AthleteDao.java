package com.ivan.dao;

import com.ivan.model.Athlete;

import java.util.List;
import java.util.Optional;

public interface AthleteDao extends GeneralDao<Long, Athlete> {

    Optional<Athlete> findByLogin(String login);

    Optional<Athlete> findById(Long id);

    List<Athlete> findAll();
}