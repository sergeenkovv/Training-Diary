package com.ivan.trainingdiary.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SecurityRequest(

        @NotBlank
        @Size(min = 3, message = "Login must be at least 3 characters long!")
        String login,

        @NotBlank
        @Size(min = 3, message = "Password must be at least 3 characters long!")
        String password) {
}