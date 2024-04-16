package com.ivan.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Represents the Training entity.
 * This class is annotated with Lombok annotations such as {@link Data}, {@link NoArgsConstructor},
 * {@link AllArgsConstructor}, and {@link Builder}.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Training {

    /**
     * The unique identifier for the training session.
     */
    private Long id;
    /**
     * The type of training session.
     */
    private TrainingType trainingType;
    /**
     * The number of sets in the training session.
     */
    private Integer setsAmount;
    /**
     * The date of the training session.
     */
    private LocalDate date;
    /**
     * The ID of the athlete associated with the training session.
     */
    private Long athleteId;
}