package com.phumlanidev.productservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * Comment: this is the placeholder for documentation.
 */
@EnableJpaAuditing(auditorAwareRef = "auditorAwareImpl")
@SpringBootApplication
public class ProductServiceApplication {

  /**
   * Comment: this is the placeholder for documentation.
   */
  public static void main(String[] args) {
    SpringApplication.run(ProductServiceApplication.class, args);
  }

}
