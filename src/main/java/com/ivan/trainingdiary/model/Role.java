package com.ivan.trainingdiary.model;

import org.springframework.security.core.GrantedAuthority;

/**
 * Represents different roles within the system.
 * These roles include client and trainer.
 *
 * @author sergeenkovv
 */
public enum Role implements GrantedAuthority {
    CLIENT,
    TRAINER;

    @Override
    public String getAuthority() {
        return name();
    }
}