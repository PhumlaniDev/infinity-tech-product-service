package com.phumlanidev.productservice.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.math.BigDecimal;

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
  @DecimalMin(value = "0.01", message = "Price must be greater than 0")
  private BigDecimal price;
  @DecimalMin(value = "1", message = "Quantity must be at least 1")
  private Integer quantity;
  private String imageUrl;

}
