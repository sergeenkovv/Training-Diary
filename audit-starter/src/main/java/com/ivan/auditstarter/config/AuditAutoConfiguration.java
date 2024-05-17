package com.ivan.auditstarter.config;

import com.ivan.auditstarter.annotation.EnableAudit;
import com.ivan.auditstarter.aspect.AuditAspect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Starter configuration class audit. Class excluded from autoconfiguration.
 * To enable it you need to add the {@link EnableAudit} annotation on configuration class.
 *
 * @author sergeenkovv
 */
@Configuration
public class AuditAutoConfiguration {
    @Bean
    public AuditAspect auditAspect() {
        return new AuditAspect();
    }
}