package com.ivan.trainingdiary.dto;

public record TrainingEditRequest(
        String athleteLogin,
        String typeName,
        Integer setsAmount,
        String date
) {
}