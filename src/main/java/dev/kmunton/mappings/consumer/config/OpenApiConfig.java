package dev.kmunton.mappings.consumer.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI openAPI(@Value("${project.version}") String appVersion) {
        return new OpenAPI().info(new Info().title("Mappings Consumer").version(appVersion));
    }

}
