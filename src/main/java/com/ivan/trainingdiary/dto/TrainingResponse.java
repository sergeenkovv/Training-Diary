package com.ivan.trainingdiary.dto;

public record TrainingResponse(
        Long id,
        Integer setsAmount,
        String trainingType,
        String date
) {
}