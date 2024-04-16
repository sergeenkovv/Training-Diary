package com.ivan.service.impl;

import com.ivan.dao.AuditDao;
import com.ivan.model.ActionType;
import com.ivan.model.Audit;
import com.ivan.service.AuditService;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.List;

/**
 * Implementation of the {@link AuditService} interface for managing audit logs.
 */
@RequiredArgsConstructor
public class AuditServiceImpl implements AuditService {

    private final AuditDao auditDao;

    /**
     * Retrieves all audit logs associated with a specific athlete's login.
     *
     * @param login The login of the athlete.
     * @return A list of audit logs.
     */
    @Override
    public List<Audit> getAllAuditsByAthleteLogin(String login) {
        return auditDao.findAllByLogin(login);
    }

    /**
     * Records an audit log for a specific action performed by an athlete.
     *
     * @param actionType The type of action performed.
     * @param login      The login of the athlete.
     */
    @Override
    public void audit(ActionType actionType, String login) {
        Audit audit = Audit.builder()
                .actionType(actionType)
                .AthleteLogin(login)
                .date(LocalDate.now())
                .build();

        auditDao.save(audit);
    }
}