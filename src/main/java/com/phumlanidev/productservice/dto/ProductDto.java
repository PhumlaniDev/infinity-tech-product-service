package com.phumlanidev.productservice.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

/**
 * Comment: this is the placeholder for documentation.
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {

  @NotEmpty(message = "Name must not be blank")
  private String name;
  @NotEmpty(message = "Description must not be blank")
  private String description;
  @NotEmpty(message = "Price is required")
  private BigDecimal price;
  @NotNull(message = "Quantity must not be null")
  private BigDecimal quantity;
  private String imageUrl;

}
