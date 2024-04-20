package com.ivan.dao.impl;

import com.ivan.containers.PostgresTestContainer;
import com.ivan.dao.AthleteDao;
import com.ivan.liquibase.LiquibaseDemo;
import com.ivan.model.Athlete;
import com.ivan.model.Role;
import com.ivan.util.ConnectionManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class AthleteDaoImplTest extends PostgresTestContainer {

    private AthleteDao athleteDao;

    @BeforeEach
    public void setup() {
        ConnectionManager connectionManager = new ConnectionManager(
                container.getJdbcUrl(),
                container.getUsername(),
                container.getPassword()
        );
        LiquibaseDemo liquibaseTest = LiquibaseDemo.getInstance();
        liquibaseTest.runMigrations(connectionManager.getConnection());

        athleteDao = new AthleteDaoImpl(connectionManager);
    }

    @Test
    void findByLogin() {
        Athlete athlete = Athlete.builder()
                .login("ivan")
                .password("123")
                .role(Role.CLIENT)
                .build();
        athleteDao.save(athlete);

        Optional<Athlete> foundAthlete = athleteDao.findByLogin("ivan");
        assertTrue(foundAthlete.isPresent());
        assertEquals("ivan", foundAthlete.get().getLogin());

        Optional<Athlete> notFoundAthlete = athleteDao.findByLogin("NonExistentLogin");
        assertFalse(notFoundAthlete.isPresent());
    }

    @Test
    void findById() {
        Athlete athlete = Athlete.builder()
                .login("ivan")
                .password("123")
                .role(Role.CLIENT)
                .build();
        athleteDao.save(athlete);

        Optional<Athlete> foundAthlete = athleteDao.findById(1L);
        assertTrue(foundAthlete.isPresent());
        assertEquals("ivan", foundAthlete.get().getLogin());

        Optional<Athlete> notFoundUser = athleteDao.findById(999L);
        assertFalse(notFoundUser.isPresent());
    }

    @Test
    void findAll() {
        Athlete athlete1 = Athlete.builder()
                .login("ivan")
                .password("123")
                .role(Role.CLIENT)
                .build();
        Athlete athlete2 = Athlete.builder()
                .login("ivan")
                .password("123")
                .role(Role.CLIENT)
                .build();

        athleteDao.save(athlete1);
        athleteDao.save(athlete2);

        List<Athlete> allAthlete = athleteDao.findAll();
        assertFalse(allAthlete.isEmpty());
    }

    @Test
    void save() {
        Athlete athleteToSave = Athlete.builder()
                .login("ivan")
                .password("123")
                .role(Role.CLIENT)
                .build();
        Athlete savedAthlete = athleteDao.save(athleteToSave);
        assertNotNull(savedAthlete.getId());
        assertEquals(athleteToSave.getLogin(), savedAthlete.getLogin());
        assertEquals(athleteToSave.getPassword(), savedAthlete.getPassword());
        assertEquals(athleteToSave.getRole(), savedAthlete.getRole());
    }
}