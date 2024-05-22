package com.ivan.trainingdiary.config;


import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * This configuration class sets up the OpenAPI documentation for the application.
 * It specifies the server URL, API information, and security requirements.
 * It also excludes certain SpringDoc configurations from component scanning.
 *
 * @author sergeenkovv
 */
@OpenAPIDefinition(
        servers = @Server(description = "Server URL in Development environment", url = "http://localhost:8080/")
)
@Configuration
public class OpenApiConfiguration {

    /**
     * This method creates an OpenAPI bean with the API information and security requirements.
     * @return the OpenAPI bean
     */
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(getApiInfo())
                .addSecurityItem(new SecurityRequirement().addList("JWT"))
                .schemaRequirement("JWT", new SecurityScheme()
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT")
                        .in(SecurityScheme.In.HEADER)
                        .name("Authorization"));
    }

    /**
     * This method creates an Info object with the API title, description, version, and contact information.
     * @return the Info object
     */
    private Info getApiInfo() {
        return new Info()
                .title("Training Diary Api Documentation")
                .description("A training diary application designed to record, view and analyze users' training data.")
                .version("0.0.1")
                .contact(new Contact()
                        .name("Ivan Sergeenkov")
                        .url("https://github.com/sergeenkovv")
                        .email("itproger181920@gmail.com"));
    }
}