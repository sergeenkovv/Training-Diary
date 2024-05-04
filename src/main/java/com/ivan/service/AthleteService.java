package com.ivan.service;

import com.ivan.model.Athlete;

import java.util.List;

/**
 * This interface provides methods to interact with athlete data.
 *
 * @author sergeenkovv
 */
public interface AthleteService {

    /**
     * Retrieves a list of all athletes.
     */
    List<Athlete> getAllAthletes();

    /**
     * Retrieves an athlete by their unique identifier.
     *
     * @param id The unique identifier of the athlete.
     */
    Athlete getById(Long id);

    /**
     * Retrieves an athlete by their login.
     *
     * @param login The unique identifier of the athlete.
     */
    Athlete getByLogin(String login);
}