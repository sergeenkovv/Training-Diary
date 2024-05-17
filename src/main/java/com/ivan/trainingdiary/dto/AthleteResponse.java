package com.ivan.trainingdiary.dto;

import com.ivan.trainingdiary.model.Role;

public record AthleteResponse(
        String login,
        Role role) {
}
