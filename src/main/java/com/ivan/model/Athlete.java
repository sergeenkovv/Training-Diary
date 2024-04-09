package com.ivan.model;

import com.ivan.model.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Athlete {

    private Long id;
    private String login;
    private String password;
    @Builder.Default
    private Role role = Role.CLIENT;
}