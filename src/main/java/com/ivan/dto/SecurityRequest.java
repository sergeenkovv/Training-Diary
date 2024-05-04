package com.ivan.dto;

public record SecurityRequest(
        String login,
        String password) {
}