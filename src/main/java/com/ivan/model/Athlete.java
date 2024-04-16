package com.ivan.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents the Athlete entity.
 * This class is annotated with Lombok annotations such as {@link Data}, {@link NoArgsConstructor},
 * {@link AllArgsConstructor}, and {@link Builder}.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Athlete {

    /**
     * The unique identifier for the athlete.
     */
    private Long id;

    /**
     * The login username of the athlete.
     */
    private String login;

    /**
     * The password of the athlete's account.
     */
    private String password;

    /**
     * The role of the athlete. Defaults to {@link Role#CLIENT}.
     */
    @Builder.Default
    private Role role = Role.CLIENT;
}