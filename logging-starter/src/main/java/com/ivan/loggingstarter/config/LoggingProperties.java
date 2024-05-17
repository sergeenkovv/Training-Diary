package com.ivan.loggingstarter.config;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Configuration properties for logging.
 *
 * @author sergeenkovv
 */
@Data
@NoArgsConstructor
@ConfigurationProperties(prefix = "app.common.logging")
public class LoggingProperties {

    private boolean enabled = true;
}