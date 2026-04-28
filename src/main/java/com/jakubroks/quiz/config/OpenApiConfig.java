package com.jakubroks.quiz.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

    @Configuration
    @OpenAPIDefinition(
            info = @Info(
                    title = "Quiz API",
                    version = "v1",
                    description = "Quiz API"
            ),
            security = @SecurityRequirement(name = "X-KEY")
    )
    @SecurityScheme(
            name = "X-KEY",
            type = SecuritySchemeType.APIKEY,
            in = SecuritySchemeIn.HEADER,
            paramName = "X-KEY",
            description = "API user key"
    )
    public class OpenApiConfig {
    }

