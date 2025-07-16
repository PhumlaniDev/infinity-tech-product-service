package com.phumlanidev.productservice.service.impl;


import com.phumlanidev.productservice.config.JwtAuthenticationConverter;
import com.phumlanidev.productservice.constant.Constant;
import com.phumlanidev.productservice.dto.ProductDto;
import com.phumlanidev.productservice.exception.ProductNotFoundException;
import com.phumlanidev.productservice.exception.product.ProductAlreadyExistsException;
import com.phumlanidev.productservice.mapper.ProductMapper;
import com.phumlanidev.productservice.model.Product;
import com.phumlanidev.productservice.repository.ProductRepository;
import com.phumlanidev.productservice.utils.ProductSpecification;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * Comment: this is the placeholder for documentation.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class ProductServiceImpl {

  private final ProductRepository productRepository;
  private final ProductMapper productMapper;
  private final HttpServletRequest request;
  private final AuditLogServiceImpl auditLogService;
  private final JwtAuthenticationConverter jwtAuthenticationConverter;

  @Transactional
  public void createProduct(ProductDto productDto) {
    if (productDto.getName() == null || productDto.getName().isEmpty()) {
      throw new IllegalArgumentException("Product name cannot be null or empty");
    }

    if (productRepository.findByName(productDto.getName()).isPresent()) {
      throw new ProductAlreadyExistsException("Product already exists");
    }

    Product product = productMapper.toEntity(productDto, new Product());

    Product savedProduct = productRepository.save(product);

    productMapper.toDto(savedProduct, productDto);
    logAudit("PRODUCT_CREATED","PRODUCT_CREATED");
  }

  /**
   * Comment: this is the placeholder for documentation.
   */
  @Transactional
  @Cacheable(value = "products", key = "#productId")
  public ProductDto findProductById(Long productId) {
    ProductDto productDto = productRepository.findById(productId)
        .map(product -> productMapper.toDto(product, new ProductDto()))
        .orElseThrow(() -> new ProductNotFoundException(Constant.PRODUCT_NOT_FOUND));
    logAudit("PRODUCT_RETRIEVED", "Product retrieved successfully");

    return productDto;
  }

  /**
   * Comment: this is the placeholder for documentation.
   */
  @Transactional
  public List<ProductDto> findAllProducts() {
    List<Product> products = productRepository.findAll();
    log.debug("Products from DB: {}", products);

    List<ProductDto> productDtos = products.stream()
            .filter(product -> product.getName() != null && !product.getName().isEmpty())
            .map(product -> productMapper.toDto(product, new ProductDto())).toList();
    log.debug("Filtered Products: {}", productDtos);
    logAudit("PRODUCTS_RETRIEVED", "All products retrieved successfully");

    return productDtos;
  }


  /**
   * Comment: this is the placeholder for documentation.
   */
  @Transactional
  @CacheEvict(value = "products", key = "#productId")
  public ProductDto updateProduct(Long productId, ProductDto productDto) {
    if (productDto.getName() == null || productDto.getName().isEmpty()) {
      throw new IllegalArgumentException("product name cannot be null or empty");
    }

    Product product = productRepository.findById(productId)
        .orElseThrow(() -> new ProductNotFoundException(Constant.PRODUCT_NOT_FOUND));

    productMapper.toEntity(productDto, product);

    Product updateProduct = productRepository.save(product);
    logAudit("PRODUCT_UPDATED", "Product updated successfully");

    return productMapper.toDto(updateProduct, new ProductDto());
  }

  /**
   * Comment: this is the placeholder for documentation.
   */
  @Transactional
  @CacheEvict(value = "products", key = "#productId")
  public void deleteProductById(Long productId) {

    productRepository.findById(productId)
        .orElseThrow(() -> new ProductNotFoundException(Constant.PRODUCT_NOT_FOUND));
    productRepository.deleteById(productId);
    logAudit("PRODUCT_DELETED", "Product deleted successfully");
  }

  /**
   * Comment: this is the placeholder for documentation.
   */
  @Cacheable(value = "productSearchCache", key = "#productName + '-' + #minPrice + " +
      "'-' + #maxPrice + '-' + #pageable.pageNumber + '-' " +
      "+ #pageable.pageSize + '-' + #pageable.sort")
  public List<ProductDto> searchProducts(String productName, BigDecimal minPrice,
                                         BigDecimal maxPrice, Pageable pageable) {
    Specification<Product> spec =
        ProductSpecification.filterProducts(productName, minPrice, maxPrice);

    Page<Product> productPage = productRepository.findAll(spec, pageable);
    log.debug("Product search results: {}", productPage.getContent());

    return productPage.getContent()
            .stream()
            .map(product -> productMapper.toDto(product, new ProductDto()))
            .toList();
  }

  public BigDecimal getProductPrice(Long productId) {
    Product product = productRepository.findById(productId)
            .orElseThrow(() -> new ProductNotFoundException(Constant.PRODUCT_NOT_FOUND));
    logAudit("PRODUCT_PRICE_RETRIEVED", "Product price retrieved successfully for product ID: "
            + productId);
    return product.getPrice();
  }

  private void logAudit(String action, String details) {
    String clientIp = request.getRemoteAddr();
    String username = jwtAuthenticationConverter.getCurrentUsername();
    String userId = jwtAuthenticationConverter.getCurrentUserId();

    auditLogService.log(
            action,
            userId,
            username,
            clientIp,
            details
    );
  }
}
