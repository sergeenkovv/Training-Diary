package com.ivan.dto;

import java.util.List;

public record TrainingTypesListResponse(
        List<TrainingTypeResponse> types) {
}