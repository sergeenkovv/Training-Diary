package com.ivan.service.impl;

import com.ivan.annotations.Auditable;
import com.ivan.annotations.Loggable;
import com.ivan.dao.AthleteDao;
import com.ivan.dto.JwtResponse;
import com.ivan.exception.AuthorizationException;
import com.ivan.exception.RegistrationException;
import com.ivan.model.Athlete;
import com.ivan.security.JwtTokenProvider;
import com.ivan.service.SecurityService;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

/**
 * Implementation of the {@link SecurityService} interface providing
 * functionality for athlete registration and authorization.
 * <p>
 * Requires an {@link AthleteDao} to be
 * injected for data access.
 *
 * @author sergeenkovv
 */
@RequiredArgsConstructor
public class SecurityServiceImpl implements SecurityService {

    private final AthleteDao athleteDao;
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * Registers a new athlete with the provided login and password.
     *
     * @param login    The login for the new athlete.
     * @param password The password for the new athlete.
     * @return The registered athlete.
     * @throws RegistrationException If an athlete with the same login already exists.
     */
    @Override
    @Loggable
    @Auditable
    public Athlete registration(String login, String password) {
        Optional<Athlete> athlete = athleteDao.findByLogin(login);

        if (athlete.isPresent()) {
            throw new RegistrationException("The athlete with this login already exists!");
        }

        Athlete newAthlete = Athlete.builder()
                .login(login)
                .password(password)
                .build();

        return athleteDao.save(newAthlete);
    }

    /**
     * Authorizes an athlete with the provided login and password.
     *
     * @param login    The login of the athlete.
     * @param password The password of the athlete.
     * @return The authorized athlete.
     * @throws AuthorizationException If no athlete with the provided login is found
     *                                or if the provided password is incorrect.
     */
    @Override
    @Loggable
    @Auditable
    public JwtResponse authorization(String login, String password) {
        Optional<Athlete> maybeAthlete = athleteDao.findByLogin(login);

        if (maybeAthlete.isEmpty()) {
            throw new AuthorizationException("There is no athlete with this login in the database!");
        }

        if (!maybeAthlete.get().getPassword().equals(password)) {
            throw new AuthorizationException("Incorrect password!");
        }

        String accessToken = jwtTokenProvider.createAccessToken(login);
        return new JwtResponse(login, accessToken);
    }
}