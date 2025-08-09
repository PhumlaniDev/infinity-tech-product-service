package com.phumlanidev.productservice.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Comment: this is the placeholder for documentation.
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

  @Value("${spring.security.oauth2.resourceserver.jwt.jwk-uri}")
  private String jwkUri;

  private final JwtAuthenticationConverter jwtAuthenticationConverter;

  /**
   * Comment: this is the placeholder for documentation.
   */
  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.csrf(csrf -> csrf
            .ignoringRequestMatchers("/actuator/**", "/api/v1/products/all", 
                    "/api/v1/products/*/price", "/api/v1/products/search"))
            .authorizeHttpRequests(auth -> auth
                    .requestMatchers("/actuator/**").permitAll()
                    .requestMatchers("/api/v1/products/all", "/api/v1/products/*/price",
                            "/api/v1/products/search").permitAll()
                    .requestMatchers("/api/v1/products/**").hasRole("admin")
                    .anyRequest().authenticated())
            .oauth2ResourceServer(oauth2 -> oauth2.jwt(
                    jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter)));

    return http.build();
  }
}
