package com.ivan.service;

import com.ivan.model.ActionType;
import com.ivan.model.Audit;

import java.util.List;

public interface AuditService {

    List<Audit> getAllAuditsByAthleteId(String athleteId);

    void audit(ActionType actionType, String login);

    Audit save(Audit audit);
}