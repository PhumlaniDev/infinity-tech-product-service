package com.phumlanidev.productservice.utils.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.Optional;

@Configuration
@EnableJpaAuditing(auditorAwareRef = "testAuditorAware")   // ← name can be anything
public class TestJpaAuditingConfig {

    @Bean("testAuditorAware")
    AuditorAware<String> testAuditorAware() {
        return () -> Optional.of("test-user");   // or Optional.empty()
        // You can also mock SecurityContext here if needed
    }
}