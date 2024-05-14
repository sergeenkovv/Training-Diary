package com.ivan.config;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * The {@code WebConfig} class is a configuration class for customizing web-related configurations in a Spring application.
 *
 * <p>This class is annotated with `@Configuration`, indicating that it provides bean definitions for custom configurations.
 *
 * <p>It implements the `WebMvcConfigurer` interface, allowing for custom configuration of web-based features in a Spring application.
 *
 * <p>In this specific configuration, the class customizes message converters used for JSON serialization to enable pretty-printing (indenting) of JSON responses.
 *
 * <p>Example usage:
 * <pre>
 * // This class is typically used to configure custom web-related settings in your Spring application.
 * public class MyWebInitializer {
 *     public static void main(String[] args) {
 *         SpringApplication.run(WebConfig.class, args);
 *     }
 * }
 * </pre>
 */
@Configuration
public class WebConfiguration implements WebMvcConfigurer {

    @Bean
    public ObjectMapper objectMapper() {
        Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder()
                .indentOutput(true)
                .modulesToInstall(new JavaTimeModule());

        return builder.build();
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(new ByteArrayHttpMessageConverter());
        converters.add(new MappingJackson2HttpMessageConverter(objectMapper()));
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");

        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }
}