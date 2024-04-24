package com.ivan.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents the Training Type entity.
 * This class is annotated with Lombok annotations such as {@link Data}, {@link NoArgsConstructor},
 * {@link AllArgsConstructor}, and {@link Builder}.
 *
 * @author sergeenkovv
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TrainingType {

    /**
     * The unique identifier for the training type.
     */
    private Long id;

    /**
     * The name of the training type.
     */
    private String typeName;
}