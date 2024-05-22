package com.ivan.trainingdiary.dto;

import java.util.List;

public record TrainingTypesListResponse(
        List<TrainingTypeResponse> types) {
}