package com.phumlanidev.productservice.service.impl;

import com.phumlanidev.productservice.config.JwtAuthenticationConverter;
import com.phumlanidev.productservice.dto.ProductDto;
import com.phumlanidev.productservice.exception.ProductNotFoundException;
import com.phumlanidev.productservice.mapper.ProductMapper;
import com.phumlanidev.productservice.model.Product;
import com.phumlanidev.productservice.publisher.ProductEventPublisher;
import com.phumlanidev.productservice.repository.ProductRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductServiceImplTest {

  @Mock private ProductRepository productRepository;
  @Mock private ProductMapper productMapper;
  @Mock private ProductEventPublisher productEventPublisher;
  @Mock private JwtAuthenticationConverter jwtAuthenticationConverter;
  @Mock private AuditLogServiceImpl auditLogService;
  @Mock private HttpServletRequest request;

  @InjectMocks private ProductServiceImpl productServiceImpl;

  @BeforeEach
  void setUp() {
    try (AutoCloseable mocks = MockitoAnnotations.openMocks(this)){
      when(request.getRemoteAddr()).thenReturn("127.0.0.1");
    } catch (Exception e) {
      fail("Failed to set up mocks: " + e.getMessage());
    }
  }

  @Test
  void createProductSuccessfully() {

    // Arrange
    ProductDto productDto = new ProductDto();
    productDto.setName("Test Product");
    productDto.setQuantity(10);

    Product product = new Product();
    product.setProductId(1L);
    product.setName("Test Product");
    product.setQuantity(10);

    when(productRepository.findByName("Test product")).thenReturn(Optional.empty());
    when(productMapper.toEntity(any(ProductDto.class), any(Product.class))).thenReturn(product);
    when(productRepository.save(any(Product.class))).thenReturn(product);
    when(jwtAuthenticationConverter.getCurrentUsername())
            .thenReturn("test-user");

    doNothing().when(auditLogService)
            .log(anyString(), anyString(), anyString(), anyString(), anyString());

    // Act
    productServiceImpl.createProduct(productDto);

    // Assert
    verify(productRepository).findByName("Test Product");
    verify(productMapper).toEntity(any(ProductDto.class), any(Product.class));
    verify(productRepository).save(any(Product.class));
    verify(productEventPublisher).publishProductCreated(
            argThat(event ->
                    event.getProductId().equals(1L)
                    && event.getName().equals("Test Product")
                    && event.getInitialQuantity() == 10
            )
    );

    verifyNoMoreInteractions(productRepository, productEventPublisher);
  }

  @Test
  void findProductById_returnProduct_whenFound() {
    // Arrange
    Long productId = 1L;
    Product product = new Product();
    product.setProductId(productId);

    ProductDto productDto = new ProductDto();
    productDto.setName("Test Product");
    productDto.setQuantity(10);

    when(productRepository.findById(productId)).thenReturn(Optional.of(product));
    when(productMapper.toDto(any(Product.class), any(ProductDto.class))).thenReturn(productDto);

    when(jwtAuthenticationConverter.getCurrentUsername())
            .thenReturn("test-user");

    doNothing().when(auditLogService)
            .log(anyString(), anyString(), anyString(), anyString(), anyString());

    // Act
    ProductDto result = productServiceImpl.findProductById(productId);

    // Assert
    assertNotNull(result);
    assertEquals("Test Product", result.getName());
    assertEquals(10, result.getQuantity());

    verify(productRepository).findById(productId);
    verify(productMapper).toDto(any(Product.class), any(ProductDto.class));
    verifyNoMoreInteractions(productRepository, productMapper);
  }

  @Test
  void findProductById_throwsException_whenNotFound() {
    // Arrange
    Long productId = 1L;

    when(productRepository.findById(productId)).thenReturn(Optional.empty());

    // Act & Assert
    Exception exception = assertThrows(RuntimeException.class, () -> {
      productServiceImpl.findProductById(productId);
    });

    assertEquals("Product not found", exception.getMessage());

    verify(productRepository).findById(productId);
    verifyNoMoreInteractions(productRepository);
  }

  @Test
  void findAllProducts_returnsOnlyProductsWithValidNames() {
    // Arrange
    Product validProductName = new Product();
    validProductName.setProductId(1L);
    validProductName.setName("Valid Product 1");

    Product invalidProductName = new Product();
    invalidProductName.setProductId(2L);
    invalidProductName.setName("");

    when(productRepository.findAll()).thenReturn(List.of(validProductName, invalidProductName));
    when(productMapper.toDto(eq(validProductName), any(ProductDto.class)))
            .thenReturn(new ProductDto());

    when(jwtAuthenticationConverter.getCurrentUsername())
            .thenReturn("test-user");

    doNothing().when(auditLogService)
            .log(anyString(), anyString(), anyString(), anyString(), anyString());

    // Act
    List<ProductDto> result = productServiceImpl.findAllProducts();

    // Assert
    assertNotNull(result);
    assertEquals(1, result.size());
//    assertTrue(result.stream().allMatch(dto -> dto.getName() != null && !dto.getName().isEmpty()));

    verify(productRepository).findAll();
    verify(productMapper, times(1)).toDto(any(Product.class), any(ProductDto.class));
    verifyNoMoreInteractions(productRepository, productMapper);
  }

  @Test
  void updateProduct_throwsException_whenNameIsInvalid() {
    ProductDto productDto = new ProductDto();
    productDto.setName("");

    assertThrows(IllegalArgumentException.class, () ->
      productServiceImpl.updateProduct(1L, productDto));

    verifyNoInteractions(productRepository);
  }

  @Test
  void updateProduct_throwsException_whenProductNotFound() {
    ProductDto productDto = new ProductDto();
    productDto.setName("Updated Product");

    when(productRepository.findById(1L)).thenReturn(Optional.empty());

    Exception exception = assertThrows(RuntimeException.class, () ->
      productServiceImpl.updateProduct(1L, productDto));

    assertEquals("Product not found", exception.getMessage());

    verify(productRepository).findById(1L);
    verifyNoMoreInteractions(productRepository);
  }

  @Test
  void updateProduct_updatesAndReturnsProductSuccessfully() {
    // Arrange
    Long productId = 1L;
    Product existingProduct = new Product();
    existingProduct.setProductId(productId);

    ProductDto productDto = new ProductDto();
    productDto.setName("Updated");

    Product updatedProduct = new Product();
    ProductDto updatedProductDto = new ProductDto();

    when(productRepository.findById(productId)).thenReturn(Optional.of(existingProduct));
    when(productRepository.save(existingProduct)).thenReturn(updatedProduct);
    when(productMapper.toDto(eq(updatedProduct), any(ProductDto.class))).thenReturn(updatedProductDto);

    when(jwtAuthenticationConverter.getCurrentUsername())
            .thenReturn("test-user");

    doNothing().when(auditLogService)
            .log(anyString(), anyString(), anyString(), anyString(), anyString());

    // Act
    ProductDto result = productServiceImpl.updateProduct(productId, productDto);

    // Assert
    assertNotNull(result);
    verify(productRepository).findById(productId);
    verify(productMapper).toEntity(productDto, existingProduct);
    verify(productRepository).save(existingProduct);
  }

  @Test
  void deleteProductById_throwsException_whenProductNotFound() {
    Long productId = 1L;

    when(productRepository.findById(productId)).thenReturn(Optional.empty());

    Exception exception = assertThrows(RuntimeException.class, () ->
      productServiceImpl.deleteProductById(productId));

    assertEquals("Product not found", exception.getMessage());

    verify(productRepository).findById(productId);
    verifyNoMoreInteractions(productRepository);
  }

  @Test
  void deleteProductById_deletesProductSuccessfully() {
    Long productId = 1L;
    Product existingProduct = new Product();
    existingProduct.setProductId(productId);

    when(productRepository.findById(productId)).thenReturn(Optional.of(existingProduct));
    when(jwtAuthenticationConverter.getCurrentUsername())
            .thenReturn("test-user");

    doNothing().when(auditLogService)
            .log(anyString(), anyString(), anyString(), anyString(), anyString());

    productServiceImpl.deleteProductById(productId);

    verify(productRepository).findById(productId);
    verify(productRepository).deleteById(productId);
  }

  @Test
  void searchProducts_returnsMappedResults() {
    Pageable pageable = PageRequest.of(0,10);

    Product product = new Product();
    Page<Product> page = new PageImpl<>(List.of(product));

    when(productRepository.findAll(any(Specification.class), eq(pageable))).thenReturn(page);
    when(productMapper.toDto(eq(product), any(ProductDto.class))).thenReturn(new ProductDto());

    List<ProductDto> result =
            productServiceImpl.searchProducts("Test", null, null, pageable);

    assertEquals(1, result.size());
    verify(productRepository).findAll(any(Specification.class), eq(pageable));
  }

  @Test
  void getProductPrice_returnsPrice() {
    Product product = new Product();
    product.setPrice(BigDecimal.valueOf(999.99));

    when(productRepository.findById(1L))
            .thenReturn(Optional.of(product));
    when(jwtAuthenticationConverter.getCurrentUsername())
            .thenReturn("test-user");

    doNothing().when(auditLogService)
            .log(anyString(), anyString(), anyString(), anyString(), anyString());

    BigDecimal price = productServiceImpl.getProductPrice(1L);

    assertEquals(BigDecimal.valueOf(999.99), price);
  }

  @Test
  void getProductPrice_throwsException_whenNotFound() {
    when(productRepository.findById(1L))
            .thenReturn(Optional.empty());

    assertThrows(ProductNotFoundException.class,
            () -> productServiceImpl.getProductPrice(1L));
  }


}