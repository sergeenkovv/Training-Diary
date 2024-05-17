package com.ivan.trainingdiary.dto;

public record TrainingRequest(
        String athleteLogin,
        String typeName,
        Integer setsAmount
) {
}