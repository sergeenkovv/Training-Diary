package com.ivan.dto;

public record TrainingRequest(
        String athleteLogin,
        String typeName,
        Integer setsAmount
) {
}