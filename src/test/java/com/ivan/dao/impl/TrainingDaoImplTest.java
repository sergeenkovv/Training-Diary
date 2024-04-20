package com.ivan.dao.impl;

import com.ivan.containers.PostgresTestContainer;
import com.ivan.dao.TrainingDao;
import com.ivan.liquibase.LiquibaseDemo;
import com.ivan.model.Training;
import com.ivan.util.ConnectionManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class TrainingDaoImplTest extends PostgresTestContainer {

    private TrainingDao trainingDao;

    @BeforeEach
    void setup() {
        ConnectionManager connectionManager = new ConnectionManager(
                container.getJdbcUrl(),
                container.getUsername(),
                container.getPassword()
        );
        LiquibaseDemo liquibaseTest = LiquibaseDemo.getInstance();
        liquibaseTest.runMigrations(connectionManager.getConnection());

        trainingDao = new TrainingDaoImpl(connectionManager);
    }

    @Test
    void findAllByAthleteId() {
        Training training1 = Training.builder()
                .setsAmount(3)
                .date(LocalDate.now())
                .typeId(4L)
                .athleteId(4L)
                .build();
        Training training2 = Training.builder()
                .setsAmount(3)
                .date(LocalDate.now())
                .typeId(4L)
                .athleteId(4L)
                .build();
        trainingDao.save(training1);
        trainingDao.save(training2);

        List<Training> auditList = trainingDao.findAllByAthleteId(4L);
        assertFalse(auditList.isEmpty());
    }

    @Test
    void findById() {
        Training training = Training.builder()
                .setsAmount(3)
                .date(LocalDate.now())
                .typeId(2L)
                .athleteId(2L)
                .build();
        trainingDao.save(training);

        Optional<Training> foundTraining = trainingDao.findById(2L);
        assertTrue(foundTraining.isPresent());
        assertEquals(3, foundTraining.get().getSetsAmount());

        Optional<Training> notFoundTraining = trainingDao.findById(999L);
        assertFalse(notFoundTraining.isPresent());
    }

    @Test
    void findByAthleteIdAndTrainingDate() {
        Long athleteId = 5L;
        LocalDate date = LocalDate.now();
        Training expectedTraining = Training.builder()
                .setsAmount(3)
                .date(date)
                .typeId(1L)
                .athleteId(athleteId)
                .build();
        trainingDao.save(expectedTraining);

        Optional<Training> result = trainingDao.findByAthleteIdAndTrainingDate(athleteId, date);

        assertTrue(result.isPresent());
        assertEquals(expectedTraining, result.get());
    }

    @Test
    void delete() {
        Training training = Training.builder()
                .setsAmount(3)
                .date(LocalDate.now())
                .typeId(1L)
                .athleteId(1L)
                .build();
        Training savedTraining = trainingDao.save(training);

        Optional<Training> training1 = trainingDao.findById(savedTraining.getId());
        assertTrue(training1.isPresent());

        trainingDao.delete(training.getId());
        Optional<Training> deletedAudit = trainingDao.findById(training.getId());
        assertFalse(deletedAudit.isPresent());
    }

    @Test
    void update() {
        Training originalTraining = Training.builder()
                .setsAmount(3)
                .date(LocalDate.now())
                .typeId(1L)
                .athleteId(1L)
                .build();
        trainingDao.save(originalTraining);

        originalTraining.setSetsAmount(5);

        trainingDao.update(originalTraining);

        Optional<Training> updatedTrainingOptional = trainingDao.findById(originalTraining.getId());
        assertTrue(updatedTrainingOptional.isPresent());

        Training updatedTraining = updatedTrainingOptional.get();
        assertEquals(3, updatedTraining.getSetsAmount());
        assertEquals(originalTraining.getDate(), updatedTraining.getDate());
        assertEquals(originalTraining.getTypeId(), updatedTraining.getTypeId());
        assertEquals(originalTraining.getAthleteId(), updatedTraining.getAthleteId());
    }

    @Test
    void save() {
        Training trainingToSave = Training.builder()
                .setsAmount(3)
                .date(LocalDate.now())
                .typeId(1L)
                .athleteId(1L)
                .build();

        Training savedTraining = trainingDao.save(trainingToSave);
        assertNotNull(savedTraining.getId());
        assertEquals(trainingToSave.getSetsAmount(), savedTraining.getSetsAmount());
        assertEquals(trainingToSave.getDate(), savedTraining.getDate());
        assertEquals(trainingToSave.getTypeId(), savedTraining.getTypeId());
        assertEquals(trainingToSave.getAthleteId(), savedTraining.getAthleteId());
    }
}