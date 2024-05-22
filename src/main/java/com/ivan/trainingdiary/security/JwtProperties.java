package com.ivan.trainingdiary.security;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Configuration properties class to hold JWT related properties for Spring Security.
 */
@Component
@Data
@ConfigurationProperties(prefix = "spring.security.jwt")
public class JwtProperties {

    /**
     * The secret key used for JWT signing and verification.
     */
    private String secret;

    /**
     * The expiration time for JWT access tokens.
     */
    private Long access;
}