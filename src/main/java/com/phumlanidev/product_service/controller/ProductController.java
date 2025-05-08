package com.phumlanidev.product_service.controller;


import com.phumlanidev.product_service.constant.Constant;
import com.phumlanidev.product_service.dto.ProductDto;
import com.phumlanidev.product_service.dto.ResponseDto;
import com.phumlanidev.product_service.service.impl.ProductServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.math.BigDecimal;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * Comment: this is the placeholder for documentation.
 */
@RestController
@RequestMapping(path = "/api/v1/products", produces = {MediaType.APPLICATION_JSON_VALUE})
@RequiredArgsConstructor
@Validated
public class ProductController {

  private final ProductServiceImpl productServiceImpl;

  /**
   * Comment: this is the placeholder for documentation.
   */
  @PostMapping("/create")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<ResponseDto> createProduct(@Valid @RequestBody ProductDto productDto) {
    productServiceImpl.createProduct(productDto);
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(new ResponseDto(Constant.STATUS_CODE_CREATED, "Product created successfully"));
  }

  /**
   * Comment: this is the placeholder for documentation.
   */
  @PatchMapping("/update/{productId}")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<ProductDto> updateProduct(@Valid @PathVariable Long productId,
                                                  @RequestBody ProductDto productDto) {
    ProductDto updatedProduct = productServiceImpl.updateProduct(productId, productDto);
    return ResponseEntity.ok(updatedProduct);

  }

  /**
   * Comment: this is the placeholder for documentation.
   */
  @GetMapping("/find/{productId}")
  @PreAuthorize("permitAll()")
  public ResponseEntity<ProductDto> findProductById(@Valid @PathVariable Long productId) {
    ProductDto product = productServiceImpl.findProductById(productId);
    return ResponseEntity.status(HttpStatus.OK).body(product);
  }

  /**
   * Comment: this is the placeholder for documentation.
   */
  @DeleteMapping("/delete/{productId}")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<ResponseDto> deleteProduct(@PathVariable Long productId) {
    productServiceImpl.deleteProductById(productId);
    return ResponseEntity.status(HttpStatus.OK)
        .body(new ResponseDto(Constant.STATUS_CODE_OK, Constant.MESSAGE_200));
  }

  /**
   * Comment: this is the placeholder for documentation.
   */
  @GetMapping("/all")
  public ResponseEntity<String> getAllProducts(HttpServletRequest request) {
//    List<ProductDto> products = productServiceImpl.findAllProducts();
//    return ResponseEntity.status(HttpStatus.OK).body(products);
    String auth = request.getHeader("Authorization");
    return ResponseEntity.ok("Auth header = " + auth);
  }

  /**
   * Comment: this is the placeholder for documentation.
   */
  //  GET /api/products/search?name=laptop&page=0&size=5&sortField=price&sortDir=desc
  @GetMapping("/search")
  public Page<ProductDto> searchProducts(@RequestParam(required = false) String productName,
                                         @RequestParam(required = false) String category,
                                         @RequestParam(required = false) BigDecimal minPrice,
                                         @RequestParam(required = false) BigDecimal maxPrice,
                                         @RequestParam(defaultValue = "0") int page,
                                         @RequestParam(defaultValue = "10") int size,
                                         @RequestParam(defaultValue = "productId") String sortField,
                                         @RequestParam(defaultValue = "asc") String sortDir) {

    Sort sort = sortDir.equalsIgnoreCase("desc") ? Sort.by(sortField).descending() :
        Sort.by(sortField).ascending();
    Pageable pageable = PageRequest.of(page, size, sort);
    return productServiceImpl.searchProducts(productName, category, minPrice, maxPrice, pageable);
  }

  @GetMapping("/secure-data")
  public ResponseEntity<String> getData(@RequestHeader("X-User-Roles") String roles) {
    if (roles.contains("ROLE_ADMIN")) {
      return ResponseEntity.ok("Access granted to admin data");
    }
    return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied");
  }


}
