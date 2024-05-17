package com.ivan.trainingdiary.dto;

public record JwtResponse(
        String login,
        String accessToken) {
}