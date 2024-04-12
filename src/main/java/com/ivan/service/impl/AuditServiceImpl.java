package com.ivan.service.impl;

import com.ivan.dao.AuditDao;
import com.ivan.model.ActionType;
import com.ivan.model.Audit;
import com.ivan.service.AuditService;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class AuditServiceImpl implements AuditService {

    private final AuditDao auditDao;

    @Override
    public List<Audit> getAllAuditsByAthleteId(String athleteId) {
        return auditDao.findAllByAthleteId(athleteId);
    }

    @Override
    public void audit(ActionType actionType, String login) {
        Audit audit = Audit.builder()
                .actionType(actionType)
                .login(login)
                .build();
        save(audit);
    }

    public Audit save(Audit audit) {
        return auditDao.save(audit);
    }
}