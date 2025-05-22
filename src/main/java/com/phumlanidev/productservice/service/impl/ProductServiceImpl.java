package com.phumlanidev.productservice.service.impl;


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
import java.math.BigDecimal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

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


  /**
   * Comment: this is the placeholder for documentation.
   */
  @Transactional
  public void createProduct(ProductDto productDto) {
    String clientIp = request.getRemoteAddr();
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    String username = auth != null ? auth.getName() : "anonymous";

    if (productDto.getName() == null || productDto.getName().isEmpty()) {
      throw new IllegalArgumentException("Product name cannot be null or empty");
    }

    if (productRepository.findByName(productDto.getName()).isPresent()) {
      throw new ProductAlreadyExistsException("Product already exists");
    }

    Product product = productMapper.toEntity(productDto, new Product());

    Product savedProduct = productRepository.save(product);

    String userId = null;
    if (auth != null && auth.getPrincipal() instanceof Jwt jwt) {
      userId = jwt.getSubject(); // Keycloak userId (UUID)
    }

    productMapper.toDto(savedProduct, productDto);
    auditLogService.log(
            "PRODUCT_CREATED",
            userId,
            username,
            clientIp,
            "Product created successfully");
  }

  /**
   * Comment: this is the placeholder for documentation.
   */
  @Transactional
  @Cacheable(value = "product", key = "#productId")
  public ProductDto findProductById(Long productId) {
    return productRepository.findById(productId)
        .map(product -> productMapper.toDto(product, new ProductDto()))
        .orElseThrow(() -> new ProductNotFoundException(Constant.PRODUCT_NOT_FOUND));
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

    return productDtos;
  }


  /**
   * Comment: this is the placeholder for documentation.
   */
  @Transactional
  public ProductDto updateProduct(Long productId, ProductDto productDto) {
    if (productDto.getName() == null || productDto.getName().isEmpty()) {
      throw new IllegalArgumentException("product name cannot be null or empty");
    }

    Product product = productRepository.findById(productId)
        .orElseThrow(() -> new ProductNotFoundException(Constant.PRODUCT_NOT_FOUND));

    productMapper.toEntity(productDto, product);

    Product updateProduct = productRepository.save(product);

    String clientIp = request.getRemoteAddr();
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    String username = auth != null ? auth.getName() : "anonymous";

    String userId = null;
    if (auth != null && auth.getPrincipal() instanceof Jwt jwt) {
      userId = jwt.getSubject(); // Keycloak userId (UUID)
    }

    auditLogService.log(
            "PRODUCT_UPDATED",
            userId,
            username,
            clientIp,
            "Product updated successfully");

    return productMapper.toDto(updateProduct, new ProductDto());
  }

  /**
   * Comment: this is the placeholder for documentation.
   */
  @Transactional
  public void deleteProductById(Long productId) {

    productRepository.findById(productId)
        .orElseThrow(() -> new ProductNotFoundException(Constant.PRODUCT_NOT_FOUND));
    productRepository.deleteById(productId);

    String clientIp = request.getRemoteAddr();
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    String username = auth != null ? auth.getName() : "anonymous";

    String userId = null;
    if (auth != null && auth.getPrincipal() instanceof Jwt jwt) {
      userId = jwt.getSubject(); // Keycloak userId (UUID)
    }

    auditLogService.log(
            "PRODUCT_DELETED",
            userId,
            username,
            clientIp,
            "Product updated successfully");
  }

  /**
   * Comment: this is the placeholder for documentation.
   */
  @Cacheable(value = "productSearchCache", key = "#name + '-' + #category + '-' + #minPrice + " +
      "'-' + #maxPrice + '-' + #pageable.pageNumber + '-' " +
      "+ #pageable.pageSize + '-' + #pageable.sort")
  public Page<ProductDto> searchProducts(String productName, String category, BigDecimal minPrice,
                                         BigDecimal maxPrice, Pageable pageable) {
    Specification<Product> spec =
        ProductSpecification.filterProducts(productName, category, minPrice, maxPrice);

    Page<Product> productPage = productRepository.findAll(spec, pageable);

    return productPage.map(product -> productMapper.toDto(product, new ProductDto()));
  }

  public BigDecimal getProductPrice(Long productId) {
    Product product = productRepository.findById(productId)
            .orElseThrow(() -> new ProductNotFoundException(Constant.PRODUCT_NOT_FOUND));
    return product.getPrice();
  }
}
