package com.ivan.dto;

import java.util.List;

public record AthletesListResponse(
        List<AthleteResponse> athletes) {
}