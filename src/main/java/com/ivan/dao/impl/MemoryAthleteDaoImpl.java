package com.ivan.dao.impl;

import com.ivan.dao.AthleteDao;
import com.ivan.model.Athlete;
import com.ivan.model.Role;

import java.util.*;

public class MemoryAthleteDaoImpl implements AthleteDao {

    private final Map<Long, Athlete> athleteMap = new HashMap<>();
    private Long id = 1L;

    public MemoryAthleteDaoImpl() {
        save(
                Athlete.builder()
                        .id(-1L)
                        .login("trainer")
                        .password("trainer")
                        .role(Role.TRAINER)
                        .build()
        );
    }

    @Override
    public Optional<Athlete> findByLogin(String login) {
        Athlete athlete;
        List<Athlete> list = new ArrayList<>(athleteMap.values());

        athlete = list.stream()
                .filter(atl -> atl.getLogin().equals(login))
                .findFirst()
                .orElse(null);
        return athlete == null ? Optional.empty() : Optional.of(athlete);
    }

    @Override
    public Optional<Athlete> findById(Long id) {
        Athlete athlete = athleteMap.get(id);
        return athlete == null ? Optional.empty() : Optional.of(athlete);
    }

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