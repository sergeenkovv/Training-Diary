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
    public Athlete registration(String username, String password) {
        Optional<Athlete> player = athleteDao.findByUsername(username);

        if (player.isPresent()) {
            throw new RegistrationException("The player with this login already exists.");
        }

        Athlete newPlayer = Athlete.builder()
                .login(username)
                .password(password)
                .build();

        return athleteDao.save(newPlayer);
    }

    @Override
    public Athlete authorization(String username, String password) {
        Optional<Athlete> maybePlayer = athleteDao.findByUsername(username);

        if (maybePlayer.isEmpty()) {
            throw new AuthorizationException("There is no player with this login in the database.");
        }

        Athlete player = maybePlayer.get();
        if (!player.getPassword().equals(password)) {
            throw new AuthorizationException("Incorrect password.");
        }
        return player;
    }
}
