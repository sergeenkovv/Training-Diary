package com.ivan.controller;

import com.ivan.exception.InvalidArgumentException;
import com.ivan.model.Athlete;
import com.ivan.service.SecurityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class SecurityController {

    private final SecurityService securityService;

    public Athlete register(String login, String password) {
        if (login == null || password == null || login.isEmpty() || login.isBlank() || password.isEmpty() || password.isBlank()) {
            throw new InvalidArgumentException("The password or login cannot be empty or consist of only spaces.");
        }

        if (password.length() < 3 || password.length() > 32) {
            throw new InvalidArgumentException("The password must be between 3 and 32 characters long.");
        }

        log.info("The athlete trying to register with login " + login + " and password " + password + ".");
        return securityService.registration(login, password);
    }

    public Athlete authorize(String login, String password) {
        if (login == null || password == null || login.isEmpty() || login.isBlank() || password.isEmpty() || password.isBlank()) {
            throw new InvalidArgumentException("The password or login cannot be empty or consist of only spaces.");
        }

        log.info("The athlete to log in with login " + login + " and password " + password + ".");
        return securityService.authorization(login, password);
    }
}