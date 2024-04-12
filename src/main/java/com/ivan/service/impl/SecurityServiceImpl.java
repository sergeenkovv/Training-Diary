package com.ivan.service.impl;

import com.ivan.dao.AthleteDao;
import com.ivan.exception.AuthorizationException;
import com.ivan.exception.RegistrationException;
import com.ivan.model.Athlete;
import com.ivan.service.SecurityService;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@RequiredArgsConstructor
public class SecurityServiceImpl implements SecurityService {

    private final AthleteDao athleteDao;
    @Override
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

    @Override
    public Athlete authorization(String login, String password) {
        Optional<Athlete> maybeAthlete = athleteDao.findByLogin(login);

        if (maybeAthlete.isEmpty()) {
            throw new AuthorizationException("There is no athlete with this login in the database!");
        }

        Athlete athlete = maybeAthlete.get();
        if (!athlete.getPassword().equals(password)) {
            throw new AuthorizationException("Incorrect password.");
        }
        return athlete;
    }
}
