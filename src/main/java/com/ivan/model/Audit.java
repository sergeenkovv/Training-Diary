package com.ivan.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Represents the Audit entity.
 * This class is annotated with Lombok annotations such as {@link Data}, {@link NoArgsConstructor},
 * {@link AllArgsConstructor}, and {@link Builder}.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Audit {

    /**
     * The unique identifier for the audit record.
     */
    private Long id;

    /**
     * The login athlete`s associated with the audit action.
     */
    private String athleteLogin;

    /**
     * The type of action performed in the audit.
     */
    private ActionType actionType;

    /**
     * The date when the audit action occurred.
     */
    private LocalDate date;
}