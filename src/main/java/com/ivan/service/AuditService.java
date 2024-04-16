package com.ivan.service;

import com.ivan.model.ActionType;
import com.ivan.model.Audit;

import java.util.List;

/**
 * This interface provides methods for auditing actions related to athletes.
 */
public interface AuditService {

    /**
     * Retrieves a list of all audit records for a specific athlete login.
     *
     * @param login The login of the athlete to get audit records for.
     */
    List<Audit> getAllAuditsByAthleteLogin(String login);

    /**
     * Records an audit action performed by an athlete.
     *
     * @param actionType The type of action that was performed.
     * @param login The login of the athlete who performed the action.
     */
    void audit(ActionType actionType, String login);
}