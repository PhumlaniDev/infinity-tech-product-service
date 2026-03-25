package com.phumlanidev.productservice.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

  @Bean
  public OpenAPI productServiceOpenAPI() {
    return new OpenAPI()
            .info(new Info()
                    .title("Product Service API")
                    .description("API documentation for Product microservice")
                    .version("1.0.0")
                    .contact(new Contact()
                            .name("Phumlani Arendse")
                            .email("support@phumlanidev.com")
                            .url("https://phumlanidev.com"))
                    .license(new License()
                            .name("Apache 2.0")
                            .url("https://www.apache.org/licenses/LICENSE-2.0")));
  }

  @Bean
  public GroupedOpenApi publicApi() {
    return GroupedOpenApi.builder()
        .group("products")
        .pathsToMatch("/api/v1/products/**")
        .build();
  }
}
