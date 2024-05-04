package com.ivan.dto;

import com.ivan.model.Role;

public record AthleteResponse(
        String login,
        Role role) {
}
