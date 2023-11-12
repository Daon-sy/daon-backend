package com.daon.backend.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    final String JWT_SCHEME_NAME = "JWT Authentication";
    final String SCHEME = "bearer";
    final String BEARER_FORMAT = "JWT";

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(apiInfo())
                .addSecurityItem(securityRequirement)
                .components(components);
    }

    private Info apiInfo() {
        return new Info()
                .title("Springdoc test!!")
                .description("Swagger UI 설명란입니다! 버전 정보를 설정 파일로 뺄까요 말까요!!, " +
                        "Request Dto 등 모든 필드에 부가 정보를 다는 것은 번거롭기도 하고 논의된 사항(시간 자원의 한계)에 맞지 않기 때문에 우선 제외했습니다.")
                .version("1.0.0");
    }

    private final SecurityRequirement securityRequirement = new SecurityRequirement()
            .addList(JWT_SCHEME_NAME);

    private final Components components = new Components()
            .addSecuritySchemes(
                    JWT_SCHEME_NAME,
                    new SecurityScheme()
                            .name(JWT_SCHEME_NAME)
                            .type(SecurityScheme.Type.HTTP)
                            .scheme(SCHEME)
                            .bearerFormat(BEARER_FORMAT)
            );
}
