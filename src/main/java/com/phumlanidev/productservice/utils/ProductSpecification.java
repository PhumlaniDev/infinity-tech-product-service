package com.phumlanidev.productservice.utils;


import com.phumlanidev.productservice.model.Product;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ProductSpecification {

  private ProductSpecification() {
  }

  public static Specification<Product> filterProducts(String name,
                                                      BigDecimal minPrice, BigDecimal maxPrice) {

    return (root, query, criteriaBuilder) -> {
      List<Predicate> predicates = new ArrayList<>();

      if (name != null && !name.isEmpty()) {
        predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("name")),
            "%" + name.toLowerCase() + "%"));
      }

//      if (category != null && !category.isEmpty()) {
//        predicates.add(criteriaBuilder.equal(criteriaBuilder.lower(root.get("category")),
//            category.toLowerCase()));
//      }

      if (minPrice != null) {
        predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("price"), minPrice));
      }

      if (maxPrice != null) {
        predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("price"), maxPrice));
      }

      return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    };
  }
}
