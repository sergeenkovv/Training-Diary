package com.ivan.dto;

public record JwtResponse(
        String login,
        String accessToken) {
}