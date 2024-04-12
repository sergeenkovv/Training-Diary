package com.ivan.dao.impl;

import com.ivan.dao.AuditDao;
import com.ivan.model.Audit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MemoryAuditDaoImpl implements AuditDao {

    private final Map<Long, Audit> auditMap = new HashMap<>();
    private Long id = 1L;

    @Override
    public List<Audit> findAllByAthleteId(String athleteId) {
        List<Audit> result = new ArrayList<>();
        for (Audit audit : auditMap.values()) {
            if (audit.getLogin().equals(athleteId)) {
                result.add(audit);
            }
        }
        return result;
    }

    @Override
    public Audit save(Audit audit) {
        audit.setId(getLastId());
        incrementId();
        auditMap.put(audit.getId(), audit);
        return auditMap.get(audit.getId());
    }

    private Long getLastId() {
        return id;
    }

    private void incrementId() {
        id++;
    }
}