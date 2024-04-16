package com.ivan.service.impl;

import com.ivan.dao.AthleteDao;
import com.ivan.exception.AthleteNotFoundException;
import com.ivan.model.Athlete;
import com.ivan.service.AthleteService;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class AthleteServiceImpl implements AthleteService {

    private final AthleteDao athleteDao;

    @Override
    public List<Athlete> showAllAthletes() {
        return athleteDao.findAll();
    }

    @Override
    public Athlete getAthleteByAthleteId(Long id) {
        return athleteDao.findById(id)
                .orElseThrow(() -> new AthleteNotFoundException("No athlete found with ID: " + id));
    }
}