package com.ivan.service;

import com.ivan.model.Athlete;

import java.util.List;

/**
 * This interface provides methods to interact with athlete data.
 */
public interface AthleteService {

    /**
     * Retrieves a list of all athletes.
     */
    List<Athlete> showAllAthletes();

    /**
     * Retrieves an athlete by their unique identifier.
     *
     * @param id The unique identifier of the athlete.
     */
    Athlete getAthleteByAthleteId(Long id);
}