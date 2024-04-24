package com.ivan.dao;

import com.ivan.model.Athlete;

import java.util.List;
import java.util.Optional;

/**
 * Interface for accessing athlete data.
 * Extends {@link GeneralDao} with key type {@link Long} and entity type {@link Athlete}.
 *
 * @author sergeenkovv
 */
public interface AthleteDao extends GeneralDao<Long, Athlete> {

    /**
     * Finds an athlete by their login.
     *
     * @param login The login of the athlete to find.
     * @return An {@link Optional} containing the athlete if found, otherwise an empty {@link Optional}.
     */
    Optional<Athlete> findByLogin(String login);

    /**
     * Finds an athlete by ID.
     *
     * @param id The ID of the athlete to find.
     * @return An {@link Optional} containing the athlete if found, otherwise an empty {@link Optional}.
     */
    Optional<Athlete> findById(Long id);

    /**
     * Retrieves all athletes stored in the data source.
     *
     * @return A {@link List} of all athletes stored in the data source.
     */
    List<Athlete> findAll();
}