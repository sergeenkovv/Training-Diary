package com.ivan.dto;

import java.time.LocalDate;

public record TrainingEditRequest(
        String athleteLogin,
        String typeName,
        Integer setsAmount,
        String date
) {
}