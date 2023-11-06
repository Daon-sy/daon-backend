package com.daon.backend.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .components(new Components())
                .info(apiInfo());
    }

    private Info apiInfo() {
        return new Info()
                .title("Springdoc test!!")
                .description("Swagger UI 설명란입니다! 버전 정보를 설정 파일로 뺄까요 말까요!!, " +
                        "Request Dto 등 모든 필드에 부가 정보를 다는 것은 번거롭기도 하고 논의된 사항(시간 자원의 한계)에 맞지 않기 때문에 우선 제외했습니다.")
                .version("1.0.0");
    }
}
