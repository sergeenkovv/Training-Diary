package com.ivan.service.impl;

import com.ivan.dao.AthleteDao;
import com.ivan.exception.AthleteNotFoundException;
import com.ivan.model.Athlete;
import com.ivan.service.AthleteService;
import lombok.RequiredArgsConstructor;

import java.util.List;

/**
 * Implementation of the {@link AthleteService} interface for managing athletes.
 */
@RequiredArgsConstructor
public class AthleteServiceImpl implements AthleteService {

    private final AthleteDao athleteDao;

    /**
     * Retrieves a list of all athletes.
     *
     * @return A list of all athletes.
     */
    @Override
    public List<Athlete> showAllAthletes() {
        return athleteDao.findAll();
    }

    /**
     * Retrieves an athlete by their ID.
     *
     * @param id The ID of the athlete.
     * @return The athlete object.
     * @throws AthleteNotFoundException if no athlete is found with the given ID.
     */
    @Override
    public Athlete getAthleteByAthleteId(Long id) {
        return athleteDao.findById(id)
                .orElseThrow(() -> new AthleteNotFoundException("No athlete found with ID: " + id));
    }
}