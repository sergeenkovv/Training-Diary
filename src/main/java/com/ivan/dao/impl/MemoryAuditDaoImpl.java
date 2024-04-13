package com.ivan.dao.impl;

import com.ivan.dao.AuditDao;
import com.ivan.model.Audit;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MemoryAuditDaoImpl implements AuditDao {

    private final Map<Long, Audit> auditMap = new HashMap<>();
    private Long id = 1L;

    @Override
    public List<Audit> findAllByAthleteLogin(String login) {
        return auditMap.values().stream()
                .filter(audit -> audit.getLogin().equals(login))
                .collect(Collectors.toList());
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