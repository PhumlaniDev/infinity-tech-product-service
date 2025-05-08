package com.phumlanidev.product_service.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final KeycloakRoleConverter keycloakRoleConverter;

    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable).authorizeHttpRequests(
                        authorize -> authorize
                                .requestMatchers("/api/v1/products/**").permitAll()
//                                .requestMatchers(HttpMethod.POST, "/api/v1/products/**").hasRole("ADMIN")
//                                .requestMatchers(HttpMethod.PUT, "/api/v1/products/**").hasRole("ADMIN")
//                                .requestMatchers(HttpMethod.DELETE, "/api/v1/products/**").hasRole("ADMIN")
                                .requestMatchers("/actuator/**").permitAll()
                                .anyRequest().authenticated()
                ).oauth2ResourceServer(
                        oauth2 ->
                                oauth2.jwt(Customizer.withDefaults()));
    return http.build();
    }
}
