package com.phumlanidev.productservice.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

/**
 * Comment: this is the placeholder for documentation.
 */
@EqualsAndHashCode(callSuper = true)
@Entity
@Table
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Product extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long productId;
  private String name;
  private String description;
  private BigDecimal price;
  private Integer quantity;
  private String imageUrl;

}
