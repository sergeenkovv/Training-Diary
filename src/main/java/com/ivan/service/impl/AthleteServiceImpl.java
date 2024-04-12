package com.ivan.service.impl;

import com.ivan.dao.AthleteDao;
import com.ivan.model.Athlete;
import com.ivan.service.AthleteService;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class AthleteServiceImpl implements AthleteService {

    private final AthleteDao athleteDao;

    @Override
    public List<Athlete> showAllAthlete() {
        return athleteDao.findAll();
    }
}