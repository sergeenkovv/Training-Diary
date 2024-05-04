package com.ivan.security;

import com.ivan.model.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Authentication {
    private String login;
    private Role role;
    private boolean isAuth;
    private String message;
}