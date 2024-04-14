package com.ivan.service;

import com.ivan.model.Athlete;

public interface SecurityService {

    Athlete registration(String login, String password);

    Athlete authorization(String login, String password);
}