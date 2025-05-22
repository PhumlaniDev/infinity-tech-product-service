package com.phumlanidev.productservice.repository;


import com.phumlanidev.productservice.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Comment: this is the placeholder for documentation.
 */
@Repository
public interface ProductRepository
    extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {

  /**
   * Comment: this is the placeholder for documentation.
   */
  Optional<Product> findByName(String productName);
}
