package com.example.storeme.global.config.json;

import org.openapitools.jackson.nullable.JsonNullableModule;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JsonConfig {
    /**
     * ObjectMapper에 JsonNullableModule을 추가
     */
    @Bean
    public Jackson2ObjectMapperBuilderCustomizer addJsonNullableModule() {
        return builder -> builder.modules(new JsonNullableModule());
    }
}
