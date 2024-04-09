package com.ivan.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Training {

    private Long id;
    private TrainingType trainingType;
    private Integer setsAmount;
    private LocalDateTime time;
    private Integer caloriesAmount;
}