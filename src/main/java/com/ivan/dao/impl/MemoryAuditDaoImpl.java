package com.ivan.dao.impl;

import com.ivan.dao.AuditDao;
import com.ivan.model.Audit;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Implementation of the {@link AuditDao} interface that stores audit data in memory.
 * This implementation uses a HashMap to store audits with their corresponding IDs.
 */
public class MemoryAuditDaoImpl implements AuditDao {

    private final Map<Long, Audit> auditMap = new HashMap<>();
    private Long id = 1L;

    /**
     * Retrieves all audits associated with a specific login.
     *
     * @param login The login for which audits are to be retrieved.
     * @return A {@link List} of audits associated with the specified login.
     */
    @Override
    public List<Audit> findAllByLogin(String login) {
        return auditMap.values().stream()
                .filter(audit -> audit.getAthleteLogin().equals(login))
                .collect(Collectors.toList());
    }

    /**
     * Saves an audit to the memory storage.
     *
     * @param audit The audit to save.
     * @return The saved audit.
     */
    @Override
    public Audit save(Audit audit) {
        audit.setId(getLastId());
        incrementId();
        auditMap.put(audit.getId(), audit);
        return auditMap.get(audit.getId());
    }

    /**
     * Retrieves the last assigned ID.
     *
     * @return The last assigned ID.
     */
    private Long getLastId() {
        return id;
    }

    /**
     * Increments the ID for the next audit.
     */
    private void incrementId() {
        id++;
    }
}