package com.ivan.trainingdiary.service;

import com.ivan.trainingdiary.dto.JwtResponse;
import com.ivan.trainingdiary.model.Athlete;

/**
 * This interface provides methods for athlete registration and authorization.
 *
 * @author sergeenkovv
 */
public interface SecurityService {

    /**
     * Registers a new athlete with the provided login and password.
     *
     * @param login    The login for the new athlete.
     * @param password The password for the new athlete.
     */
    Athlete registration(String login, String password);

    /**
     * Authorizes an athlete with the provided login and password.
     *
     * @param login    The login of the athlete.
     * @param password The password of the athlete.
     */
    JwtResponse authorization(String login, String password);
}