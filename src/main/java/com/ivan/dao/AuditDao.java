package com.ivan.dao;

import com.ivan.model.Audit;

import java.util.List;

public interface AuditDao extends GeneralDao<Long, Audit> {

    List<Audit> findAllByAthleteLogin(String login);
}