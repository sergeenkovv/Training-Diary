package com.ivan.dao.impl;

import com.ivan.containers.PostgresTestContainer;
import com.ivan.dao.AthleteDao;
import com.ivan.dao.TrainingDao;
import com.ivan.dao.TrainingTypeDao;
import com.ivan.liquibase.LiquibaseMigration;
import com.ivan.model.Athlete;
import com.ivan.model.Role;
import com.ivan.model.Training;
import com.ivan.model.TrainingType;
import com.ivan.util.ConnectionManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("trainingDao implementation test")
class TrainingDaoImplTest extends PostgresTestContainer {

    private final AthleteDao athleteDao;
    private final TrainingDao trainingDao;
    private final TrainingTypeDao trainingTypeDao;

    public TrainingDaoImplTest() {
        ConnectionManager connectionManager = new ConnectionManager(
                container.getJdbcUrl(),
                container.getUsername(),
                container.getPassword()
        );
        LiquibaseMigration liquibaseTest = LiquibaseMigration.getInstance();
        liquibaseTest.runMigrations(connectionManager.getConnection());

        athleteDao = new AthleteDaoImpl(connectionManager);
        trainingTypeDao = new TrainingTypeDaoImpl(connectionManager);
        trainingDao = new TrainingDaoImpl(connectionManager, trainingTypeDao, athleteDao);
    }

    Athlete athlete;
    Training training1;
    Training training2;
    Training training3;
    TrainingType trainingType;

    @BeforeEach
    void setUp() {
        athlete = Athlete.builder()
                .id(1L)
                .login("Ivan")
                .password("1234")
                .role(Role.CLIENT)
                .build();
        athleteDao.save(athlete);

        trainingType = TrainingType.builder()
                .id(1L)
                .typeName("LEGS")
                .build();
        trainingTypeDao.save(trainingType);

        training1 = Training.builder()
                .id(1L)
                .setsAmount(3)
                .date(LocalDate.now())
                .trainingType(trainingType)
                .athlete(athlete)
                .build();
        trainingDao.save(training1);

        training2 = Training.builder()
                .id(2L)
                .setsAmount(8)
                .date(LocalDate.parse("2022-11-11"))
                .trainingType(trainingType)
                .athlete(athlete)
                .build();
        trainingDao.save(training2);

        training3 = Training.builder()
                .id(3L)
                .setsAmount(5)
                .date(LocalDate.parse("2020-11-11"))
                .trainingType(trainingType)
                .athlete(athlete)
                .build();
        trainingDao.save(training3);
    }

    @DisplayName("findAllByAthleteId method verification test")
    @Test
    void findAllByAthleteId() {
        List<Training> trainingList = trainingDao.findAllByAthleteId(training1.getAthlete().getId());
        assertFalse(trainingList.isEmpty());
        assertEquals(3, trainingList.size());
    }

    @DisplayName("findById method verification test")
    @Test
    void findById() {
        Optional<Training> foundTraining = trainingDao.findById(training2.getId());
        assertTrue(foundTraining.isPresent());
        assertEquals(8, foundTraining.get().getSetsAmount());

        Optional<Training> notFoundTraining = trainingDao.findById(999L);
        assertFalse(notFoundTraining.isPresent());
    }

    @DisplayName("findByAthleteIdAndTrainingDate method verification test")
    @Test
    void findByAthleteIdAndTrainingDate() {
        Optional<Training> foundTraining = trainingDao.findByAthleteIdAndTrainingDate(training2.getAthlete().getId(), LocalDate.parse("2022-11-11"));
        assertTrue(foundTraining.isPresent());
        assertEquals(8, foundTraining.get().getSetsAmount());

        Optional<Training> notFoundTraining = trainingDao.findByAthleteIdAndTrainingDate(999L, LocalDate.parse("2022-11-11"));
        assertFalse(notFoundTraining.isPresent());
    }

    @DisplayName("delete method verification test")
    @Test
    void delete() {
        Optional<Training> training = trainingDao.findById(training3.getId());
        assertTrue(training.isPresent());

        trainingDao.delete(training3.getId());
        Optional<Training> deletedAudit = trainingDao.findById(training3.getId());
        assertFalse(deletedAudit.isPresent());
    }

    @DisplayName("update method verification test")
    @Test
    void update() {
        training1.setSetsAmount(5);

        trainingDao.update(training1);

        Optional<Training> updatedTrainingOptional = trainingDao.findById(training1.getId());
        assertTrue(updatedTrainingOptional.isPresent());

        Training updatedTraining = updatedTrainingOptional.get();
        assertEquals(3, updatedTraining.getSetsAmount());
        assertEquals(training1.getDate(), updatedTraining.getDate());
        assertEquals(training1.getTrainingType(), updatedTraining.getTrainingType());
        assertEquals(training1.getAthlete(), updatedTraining.getAthlete());
    }

    @DisplayName("save method verification test")
    @Test
    void save() {
        Training savedTraining = trainingDao.save(training1);
        assertNotNull(savedTraining.getId());
        assertEquals(training1.getSetsAmount(), savedTraining.getSetsAmount());
        assertEquals(training1.getDate(), savedTraining.getDate());
        assertEquals(training1.getTrainingType(), savedTraining.getTrainingType());
        assertEquals(training1.getAthlete(), savedTraining.getAthlete());
    }
}