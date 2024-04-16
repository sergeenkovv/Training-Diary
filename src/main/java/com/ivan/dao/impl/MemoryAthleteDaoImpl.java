package com.ivan.dao.impl;

import com.ivan.dao.AthleteDao;
import com.ivan.model.Athlete;
import com.ivan.model.Role;

import java.util.*;

/**
 * Implementation of the {@link AthleteDao} interface that stores athlete data in memory.
 * This implementation uses a HashMap to store athletes with their corresponding IDs.
 */
public class MemoryAthleteDaoImpl implements AthleteDao {

    private final Map<Long, Athlete> athleteMap = new HashMap<>();
    private Long id = 1L;

    /**
     * Constructs a new MemoryAthleteDaoImpl instance.
     * Initializes the DAO with a predefined trainer account.
     */
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

    /**
     * Retrieves an athlete by their login.
     *
     * @param login The login of the athlete to find.
     * @return An {@link Optional} containing the athlete if found, otherwise an empty {@link Optional}.
     */
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

    /**
     * Retrieves an athlete by their ID.
     *
     * @param id The ID of the athlete to find.
     * @return An {@link Optional} containing the athlete if found, otherwise an empty {@link Optional}.
     */
    @Override
    public Optional<Athlete> findById(Long id) {
        Athlete athlete = athleteMap.get(id);
        return athlete == null ? Optional.empty() : Optional.of(athlete);
    }

    /**
     * Retrieves all athletes stored in memory.
     *
     * @return A {@link List} of all athletes stored in memory.
     */
    public List<Athlete> findAll() {
        return List.copyOf(athleteMap.values());
    }

    /**
     * Saves an athlete to the memory storage.
     *
     * @param athlete The athlete to save.
     * @return The saved athlete.
     */
    @Override
    public Athlete save(Athlete athlete) {
        athlete.setId(getLastId());
        incrementId();
        athleteMap.put(athlete.getId(), athlete);
        return athleteMap.get(athlete.getId());
    }

    /**
     * Retrieves the last assigned ID.
     *
     * @return The last assigned ID.
     */
    private Long getLastId() {
        return id;
    }

    /**
     * Increments the ID for the next athlete.
     */
    private void incrementId() {
        id++;
    }
}