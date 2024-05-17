//package com.ivan.dao.impl;
//
//import com.ivan.containers.PostgresTestContainer;
//import com.ivan.dao.AthleteDao;
//import com.ivan.liquibase.LiquibaseMigration;
//import com.ivan.model.Athlete;
//import com.ivan.model.Role;
//import com.ivan.util.ConnectionManager;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@DisplayName("athleteDao implementation test")
//class AthleteDaoImplTest extends PostgresTestContainer {
//
//    private final AthleteDao athleteDao;
//
//    public AthleteDaoImplTest() {
//        ConnectionManager connectionManager = new ConnectionManager(
//                container.getJdbcUrl(),
//                container.getUsername(),
//                container.getPassword(),
//                container.getDriverClassName()
//        );
//        LiquibaseMigration liquibaseTest = new LiquibaseMigration(connectionManager.getConnection(), "db/changelog/changelog.xml", "migration");
//        liquibaseTest.runMigrations();
//
//        athleteDao = new AthleteDaoImpl(connectionManager);
//    }
//
//    private Athlete athlete1;
//    private Athlete athlete2;
//
//    @BeforeEach
//    void setup() {
//        athlete1 = Athlete.builder()
//                .id(1L)
//                .login("Ivan")
//                .password("1234")
//                .role(Role.CLIENT)
//                .build();
//        athleteDao.save(athlete1);
//
//        athlete2 = Athlete.builder()
//                .id(2L)
//                .login("Lesha")
//                .password("4321")
//                .role(Role.CLIENT)
//                .build();
//        athleteDao.save(athlete1);
//    }
//
//    @DisplayName("findAllByLogin verification test")
//    @Test
//    void findByLogin() {
//        Optional<Athlete> foundAthlete = athleteDao.findByLogin(athlete1.getLogin());
//        assertTrue(foundAthlete.isPresent());
//        assertEquals("Ivan", foundAthlete.get().getLogin());
//
//        Optional<Athlete> notFoundAthlete = athleteDao.findByLogin("NonExistentLogin");
//        assertFalse(notFoundAthlete.isPresent());
//    }
//
//    @DisplayName("findById verification test")
//    @Test
//    void findById() {
//        Optional<Athlete> foundAthlete = athleteDao.findById(athlete1.getId());
//        assertTrue(foundAthlete.isPresent());
//        assertEquals("Ivan", foundAthlete.get().getLogin());
//
//        Optional<Athlete> notFoundUser = athleteDao.findById(999L);
//        assertFalse(notFoundUser.isPresent());
//    }
//
//    @DisplayName("findAll verification test")
//    @Test
//    void findAll() {
//        List<Athlete> allAthlete = athleteDao.findAll();
//        assertFalse(allAthlete.isEmpty());
//        assertEquals(3, allAthlete.size());
//    }
//
//    @DisplayName("save verification test")
//    @Test
//    void save() {
//        Athlete savedAthlete = athlete2;
//        assertNotNull(savedAthlete.getId());
//        assertEquals(athlete2.getLogin(), savedAthlete.getLogin());
//        assertEquals(athlete2.getPassword(), savedAthlete.getPassword());
//        assertEquals(athlete2.getRole(), savedAthlete.getRole());
//    }
//}