package com.ivan.security;

import com.ivan.model.Role;
import lombok.experimental.UtilityClass;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

/**
 * Utility class for security-related operations.
 */
@UtilityClass
public class SecurityUtils {

    /**
     * Checks if the provided login is valid based on the current security context.
     *
     * @param login The login to be validated.
     * @return true if the login is valid, false otherwise.
     * @throws SecurityException If the user is not authenticated.
     */
    public boolean isValidLogin(String login) {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        if (authentication == null) throw new SecurityException("Unauthorized!");
        User principal = (User) authentication.getPrincipal();
        return principal.getUsername().equals(login);
    }
}