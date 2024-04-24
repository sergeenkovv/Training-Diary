package com.ivan.dao;

import com.ivan.model.Audit;

import java.util.List;

/**
 * Interface for accessing audit data.
 * Extends {@link GeneralDao} with key type {@link Long} and entity type {@link Audit}.
 *
 * @author sergeenkovv
 */
public interface AuditDao extends GeneralDao<Long, Audit> {

    /**
     * Finds all audits associated with a specific login.
     *
     * @param athleteLogin The login for which audits are to be retrieved.
     * @return A {@link List} of audits associated with the specified login.
     */
    List<Audit> findAllByLogin(String athleteLogin);
}