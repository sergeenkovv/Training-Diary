package com.ivan.dao.impl;

import com.ivan.containers.PostgresTestContainer;
import com.ivan.dao.TrainingTypeDao;
import com.ivan.liquibase.LiquibaseDemo;
import com.ivan.model.TrainingType;
import com.ivan.util.ConnectionManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class TrainingTypeDaoImplTest extends PostgresTestContainer {

    private TrainingTypeDao trainingTypeDao;

    @BeforeEach
    public void setup() {
        ConnectionManager connectionManager = new ConnectionManager(
                container.getJdbcUrl(),
                container.getUsername(),
                container.getPassword()
        );
        LiquibaseDemo liquibaseTest = LiquibaseDemo.getInstance();
        liquibaseTest.runMigrations(connectionManager.getConnection());

        trainingTypeDao = new TrainingTypeDaoImpl(connectionManager);
    }

    @Test
    void findAll() {
        TrainingType expectedType1 = TrainingType.builder()
                .typeName("SHOULDERS")
                .build();
        TrainingType expectedType2 = TrainingType.builder()
                .typeName("BACK")
                .build();

        trainingTypeDao.save(expectedType1);
        trainingTypeDao.save(expectedType2);

        List<TrainingType> trainingTypes = trainingTypeDao.findAll();

        assertTrue(trainingTypes.contains(expectedType1));
        assertTrue(trainingTypes.contains(expectedType2));
    }

    @Test
    void findById() {
        TrainingType expectedType = TrainingType.builder()
                .typeName("BACK")
                .build();

        trainingTypeDao.save(expectedType);

        Optional<TrainingType> foundType = trainingTypeDao.findById(expectedType.getId());
        assertTrue(foundType.isPresent());
        assertEquals(expectedType.getTypeName(), foundType.get().getTypeName());

        Optional<TrainingType> notFoundType = trainingTypeDao.findById(999L);
        assertFalse(notFoundType.isPresent());
    }

    @Test
    void delete() {
        TrainingType trainingType = TrainingType.builder()
                .typeName("CHEST")
                .build();
        TrainingType savedTrainingType = trainingTypeDao.save(trainingType);

        Optional<TrainingType> training1 = trainingTypeDao.findById(savedTrainingType.getId());
        assertTrue(training1.isPresent());

        trainingTypeDao.delete(trainingType.getId());
        Optional<TrainingType> deletedAudit = trainingTypeDao.findById(trainingType.getId());
        assertFalse(deletedAudit.isPresent());
    }

    @Test
    void save() {
        TrainingType trainingTypeToSave = TrainingType.builder()
                .typeName("CHEST")
                .build();

        TrainingType savedtrainingType = trainingTypeDao.save(trainingTypeToSave);
        assertNotNull(savedtrainingType.getId());
        assertEquals(trainingTypeToSave.getTypeName(), savedtrainingType.getTypeName());
    }
}