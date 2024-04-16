package com.ivan.service;

import com.ivan.model.ActionType;
import com.ivan.model.Audit;

import java.util.List;

public interface AuditService {

    List<Audit> getAllAuditsByAthleteLogin(String login);

    void audit(ActionType actionType, String login);
}