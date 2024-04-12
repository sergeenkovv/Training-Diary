package com.ivan.service;

import com.ivan.model.Athlete;

import java.util.List;
import java.util.Optional;

public interface AthleteService {

    List<Athlete> showAllAthlete();

    Optional<Athlete> getAthleteById(Long id);
}