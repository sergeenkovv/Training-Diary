package com.ivan.dto;

import java.util.List;

public record AuditHistoryResponse(
        String athleteLogin,
        List<AuditResponse> trainings) {
}
