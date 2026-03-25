package com.phumlanidev.productservice.mapper;


import com.phumlanidev.productservice.dto.ProductDto;
import com.phumlanidev.productservice.model.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductMapper {

  public Product toEntity(ProductDto dto, Product product) {
    product.setName(dto.getName());
    product.setDescription(dto.getDescription());
    product.setPrice(dto.getPrice());
    product.setQuantity(dto.getQuantity());
    product.setImageUrl(dto.getImageUrl());

    return product;
  }

  public ProductDto toDto(Product product, ProductDto dto) {
    dto.setName(product.getName());
    dto.setDescription(product.getDescription());
    dto.setPrice(product.getPrice());
    dto.setQuantity(product.getQuantity());
    dto.setImageUrl(product.getImageUrl());

    return dto;
  }
}
