package com.ivan.dao.impl;

import com.ivan.dao.AthleteDao;
import com.ivan.model.Athlete;

import java.util.*;

public class MemoryAthleteDaoImpl implements AthleteDao {

    private final Map<Long, Athlete> athleteMap = new HashMap<>();
    private Long id = 1L;

    @Override
    public Optional<Athlete> findByUsername(String username) {
        Athlete athlete = null;
        List<Athlete> list = new ArrayList<>(athleteMap.values());

        for (Athlete atl : list) {
            if (atl.getLogin().equals(username)) {
                athlete = atl;
                break;
            }
        }
        return athlete == null ? Optional.empty() : Optional.of(athlete);
    }

    @Override
    public List<Athlete> findAll() {
        return List.copyOf(athleteMap.values());
    }

    @Override
    public Athlete save(Athlete athlete) {
        athlete.setId(getLastId());
        incrementId();
        athleteMap.put(athlete.getId(), athlete);
        return athleteMap.get(athlete.getId());
    }

    private Long getLastId() {
        return id;
    }

    private void incrementId() {
        id++;
    }
}