package com.pet.adoption.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("宠物领养系统 API文档")
                        .description("宠物领养平台后端接口文档")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("开发团队")
                                .url("http://localhost:8080")));
    }
}
