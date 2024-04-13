package com.ivan.service.impl;

import com.ivan.dao.AthleteDao;
import com.ivan.exception.AthleteNotFoundException;
import com.ivan.model.Athlete;
import com.ivan.service.AthleteService;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class AthleteServiceImpl implements AthleteService {

    private final AthleteDao athleteDao;

    @Override
    public List<Athlete> showAllAthlete() {
        return athleteDao.findAll();
    }

    @Override
    public Optional<Athlete> getAthleteById(Long id) {
        return Optional.ofNullable(athleteDao.findById(id)
                .orElseThrow(() -> new AthleteNotFoundException("Athlete with id " + id + " not found!")));
    }
}