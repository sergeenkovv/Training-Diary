package com.ivan.trainingdiary.dto;

import java.util.List;

public record TrainingHistoryResponse(
        String athleteLogin,
        List<TrainingResponse> trainings) {
}