package com.site.churaibe.global.config;

import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration

public class SwaggerConfig {
    @Bean
    public OpenAPI openAPI() {
        // API 기본 정보 설정 (제목, 설명, 버전)
        Info apiInfo = new Info()
                .title("My Spring Boot 3.x API")
                .description("Springdoc 3.0.x 버전을 사용한 API 명세서입니다.")
                .version("v1.0.0");

        return new OpenAPI()
                .info(apiInfo);
    }
}