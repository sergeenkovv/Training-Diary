package com.ivan.service;

import com.ivan.model.Athlete;

/**
 * This interface provides methods for athlete registration and authorization.
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
    Athlete authorization(String login, String password);
}