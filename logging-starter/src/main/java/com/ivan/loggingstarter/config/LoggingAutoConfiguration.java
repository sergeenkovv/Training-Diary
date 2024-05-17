package com.ivan.loggingstarter.config;

import com.ivan.loggingstarter.aspects.LoggingMethodExecutionAspect;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * Autoconfiguration for logging.
 * Enables the {@link LoggingMethodExecutionAspect} if the app.common.logging.enabled property is set to true.
 *
 * @author sergeenkovv
 */
@Configuration
@EnableConfigurationProperties(LoggingProperties.class)
@ConditionalOnProperty(prefix = "app.common.logging", name = "enabled", havingValue = "true")
@EnableAspectJAutoProxy
public class LoggingAutoConfiguration {

    /**
     * Bean definition for the {@code LoggingMethodExecutionAspect}.
     *
     * @return Instance of {@code LoggingMethodExecutionAspect}
     */
    @Bean
    LoggingMethodExecutionAspect loggingMethodExecutionAspect() {
        return new LoggingMethodExecutionAspect();
    }
}