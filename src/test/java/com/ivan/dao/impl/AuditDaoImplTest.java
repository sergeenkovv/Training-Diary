package com.ivan.dao.impl;

import com.ivan.containers.PostgresTestContainer;
import com.ivan.dao.AuditDao;
import com.ivan.liquibase.LiquibaseMigration;
import com.ivan.model.ActionType;
import com.ivan.model.Audit;
import com.ivan.util.ConnectionManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("auditDao implementation test")
class AuditDaoImplTest extends PostgresTestContainer {

    private final AuditDao auditDao;

    public AuditDaoImplTest() {
        ConnectionManager connectionManager = new ConnectionManager(
                container.getJdbcUrl(),
                container.getUsername(),
                container.getPassword()
        );
        LiquibaseMigration liquibaseTest = LiquibaseMigration.getInstance();
        liquibaseTest.runMigrations(connectionManager.getConnection());

        auditDao = new AuditDaoImpl(connectionManager);
    }

    @DisplayName("findAllByLogin verification test")
    @Test
    void findAllByLogin() {
        Audit audit1 = Audit.builder()
                .id(11L)
                .athleteLogin("ivan")
                .actionType(ActionType.ADD_TRAINING)
                .date(LocalDate.now())
                .build();
        Audit audit2 = Audit.builder()
                .id(10L)
                .athleteLogin("ivan")
                .actionType(ActionType.ADD_TRAINING)
                .date(LocalDate.now())
                .build();
        auditDao.save(audit1);
        auditDao.save(audit2);

        List<Audit> auditList = auditDao.findAllByLogin("ivan");
        assertFalse(auditList.isEmpty());
    }

    @DisplayName("save method verification test")
    @Test
    void save() {
        Audit auditToSave = Audit.builder()
                .id(12L)
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