package com.ivan.security;

import com.ivan.util.YamlPropertySourceFactory;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * Configuration properties class to hold JWT related properties for Spring Security.
 */
@Component
@Data
@PropertySource(value = "classpath:application.yaml", factory = YamlPropertySourceFactory.class)
public class JwtProperties {

    /**
     * The secret key used for JWT signing and verification.
     */
    @Value("${jwt.secret}")
    private String secret;

    /**
     * The expiration time for JWT access tokens.
     */
    @Value("${jwt.access}")
    private Long access;
}