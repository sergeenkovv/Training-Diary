package com.ivan.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Training {

    private Long id;
    private TrainingType trainingType;
    private Integer setsAmount;
    private LocalDate date;
    private Long athleteId;
}