package com.ivan.service.impl;

import com.ivan.dao.AuditDao;
import com.ivan.model.ActionType;
import com.ivan.model.Audit;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuditServiceImplTest {

    @InjectMocks
    private AuditServiceImpl auditServiceImpl;

    @Mock
    private AuditDao auditDao;

    @Test
    void getAllAuditsByAthleteLogin_Success() {
        String login = "Ivan";

        Audit audit1 = Audit.builder()
                .id(1L)
                .login(login)
                .actionType(ActionType.ADD_TRAINING)
                .date(LocalDate.now())
                .build();

        Audit audit2 = Audit.builder()
                .id(2L)
                .login(login)
                .actionType(ActionType.REGISTRATION)
                .date(LocalDate.now())
                .build();

        List<Audit> expectedAudits = Arrays.asList(audit1, audit2);

        when(auditDao.findAllByLogin(login)).thenReturn(expectedAudits);

        List<Audit> result = auditServiceImpl.getAllAuditsByAthleteLogin(login);

        assertThat(result).isEqualTo(expectedAudits);
    }

    @Test
    void audit_Success() {
        String login = "Ivan";
        ActionType actionType = ActionType.AUTHORIZATION;

        auditServiceImpl.audit(actionType, login);

        verify(auditDao, times(1)).save(any(Audit.class));
    }
}