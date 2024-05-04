package com.ivan.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;

public record TrainingResponse(
        Long id,
        Integer setsAmount,
        String trainingType,
        String date
) {
}