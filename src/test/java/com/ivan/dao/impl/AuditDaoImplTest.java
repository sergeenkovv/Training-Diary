package com.ivan.dao.impl;

import com.ivan.containers.PostgresTestContainer;
import com.ivan.dao.AuditDao;
import com.ivan.liquibase.LiquibaseDemo;
import com.ivan.model.ActionType;
import com.ivan.model.Athlete;
import com.ivan.model.Audit;
import com.ivan.util.ConnectionManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AuditDaoImplTest extends PostgresTestContainer {

    private AuditDao auditDao;

    @BeforeEach
    public void setup() {
        ConnectionManager connectionManager = new ConnectionManager(
                container.getJdbcUrl(),
                container.getUsername(),
                container.getPassword()
        );
        LiquibaseDemo liquibaseTest = LiquibaseDemo.getInstance();
        liquibaseTest.runMigrations(connectionManager.getConnection());

        auditDao = new AuditDaoImpl(connectionManager);
    }

    @Test
    void findAllByLogin() {
        Audit audit1 = Audit.builder()
                .athleteLogin("ivan")
                .actionType(ActionType.ADD_TRAINING)
                .date(LocalDate.now())
                .build();
        Audit audit2 = Audit.builder()
                .athleteLogin("ivan")
                .actionType(ActionType.ADD_TRAINING)
                .date(LocalDate.now())
                .build();
        auditDao.save(audit1);
        auditDao.save(audit2);

        List<Audit> auditList = auditDao.findAllByLogin("ivan");
        assertFalse(auditList.isEmpty());
    }

    @Test
    void save() {
        Audit auditToSave = Audit.builder()
                .athleteLogin("ivan")
                .actionType(ActionType.ADD_TRAINING)
                .date(LocalDate.now())
                .build();

        Audit savedAudit = auditDao.save(auditToSave);
        assertNotNull(savedAudit.getId());
        assertEquals(auditToSave.getAthleteLogin(), savedAudit.getAthleteLogin());
        assertEquals(auditToSave.getActionType(), savedAudit.getActionType());
        assertEquals(auditToSave.getDate(), savedAudit.getDate());
    }
}