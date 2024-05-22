//package com.ivan.trainingdiary.dao.impl;
//
//import com.ivan.containers.PostgresTestContainer;
//import com.ivan.trainingdiary.dao.TrainingTypeRepository;
//import com.ivan.liquibase.LiquibaseMigration;
//import com.ivan.trainingdiary.model.TrainingType;
//import com.ivan.trainingdiary.util.ConnectionManager;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//
//import java.util.Arrays;
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.anyString;
//
//@DisplayName("trainingTypeDao implementation test")
//class TrainingTypeDaoImplTest extends PostgresTestContainer {
//
//    private final TrainingTypeRepository trainingTypeDao;
//
//    public TrainingTypeDaoImplTest() {
//        ConnectionManager connectionManager = new ConnectionManager(
//                container.getJdbcUrl(),
//                container.getUsername(),
//                container.getPassword(),
//                container.getDriverClassName()
//        );
//        LiquibaseMigration liquibaseTest = new LiquibaseMigration(connectionManager.getConnection(), "db/changelog/changelog.xml", "migration");
//        liquibaseTest.runMigrations();
//
//        trainingTypeDao = new TrainingTypeRepositoryImpl(connectionManager);
//    }
//
//    List<TrainingType> mockTrainingTypes;
//    TrainingType trainingType1;
//    TrainingType trainingType2;
//
//    @BeforeEach
//    void setUp() {
//        trainingType1 = TrainingType.builder()
//                .id(1L)
//                .typeName("CHEST")
//                .build();
//        trainingType2 = TrainingType.builder()
//                .id(2L)
//                .typeName("LEGS")
//                .build();
//
//        trainingTypeDao.save(trainingType1);
//        trainingTypeDao.save(trainingType2);
//
//        mockTrainingTypes = Arrays.asList(trainingType1, trainingType2);
//    }
//
//    @DisplayName("findAll method verification test")
//    @Test
//    void findAll() {
//        List<TrainingType> trainingTypes = trainingTypeDao.findAll();
//
//        assertTrue(trainingTypes.contains(trainingType1));
//        assertTrue(trainingTypes.contains(trainingType2));
//    }
//
//    @DisplayName("findById method verification test")
//    @Test
//    void findById() {
//        Optional<TrainingType> foundType = trainingTypeDao.findById(trainingType1.getId());
//        assertTrue(foundType.isPresent());
//        assertEquals(trainingType1.getTypeName(), foundType.get().getTypeName());
//
//        Optional<TrainingType> notFoundType = trainingTypeDao.findById(999L);
//        assertFalse(notFoundType.isPresent());
//    }
//
//    @DisplayName("findByTypeName method verification test")
//    @Test
//    void findByTypeName() {
//        Optional<TrainingType> foundType = trainingTypeDao.findByTypeName(trainingType1.getTypeName());
//        assertTrue(foundType.isPresent());
//        assertEquals(trainingType1.getTypeName(), foundType.get().getTypeName());
//
//        Optional<TrainingType> notFoundType = trainingTypeDao.findByTypeName(String.valueOf(Optional.empty()));
//        assertFalse(notFoundType.isPresent());
//    }
//
//    @DisplayName("delete method verification test")
//    @Test
//    void delete() {
//        TrainingType savedTrainingType = trainingType1;
//
//        Optional<TrainingType> training1 = trainingTypeDao.findById(savedTrainingType.getId());
//        assertTrue(training1.isPresent());
//
//        trainingTypeDao.delete(trainingType1.getId());
//        Optional<TrainingType> deletedAudit = trainingTypeDao.findById(trainingType1.getId());
//        assertFalse(deletedAudit.isPresent());
//    }
//
//    @DisplayName("save method verification test")
//    @Test
//    void save() {
//        TrainingType savedtrainingType = trainingType1;
//        assertNotNull(savedtrainingType.getId());
//        assertEquals(trainingType1.getTypeName(), savedtrainingType.getTypeName());
//    }
//}