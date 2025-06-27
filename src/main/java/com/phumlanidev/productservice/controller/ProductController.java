package com.phumlanidev.productservice.controller;


import com.phumlanidev.productservice.constant.Constant;
import com.phumlanidev.productservice.dto.ProductDto;
import com.phumlanidev.productservice.dto.ResponseDto;
import com.phumlanidev.productservice.service.impl.ProductServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
@Validated
public class ProductController {

  private final ProductServiceImpl productServiceImpl;

  @PostMapping("/create")
  public ResponseEntity<ResponseDto> createProduct(@Valid @RequestBody ProductDto productDto) {
    productServiceImpl.createProduct(productDto);
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(new ResponseDto(Constant.STATUS_CODE_CREATED, "Product created successfully"));
  }

  @PatchMapping("/update/{productId}")
  public ResponseEntity<ProductDto> updateProduct(@Valid @PathVariable Long productId,
                                                  @RequestBody ProductDto productDto) {
    ProductDto updatedProduct = productServiceImpl.updateProduct(productId, productDto);
    return ResponseEntity.ok(updatedProduct);

  }

  @GetMapping("/find/{productId}")
  public ResponseEntity<ProductDto> findProductById(@Valid @PathVariable Long productId) {
    ProductDto product = productServiceImpl.findProductById(productId);
    return ResponseEntity.status(HttpStatus.OK).body(product);
  }

  @DeleteMapping("/delete/{productId}")
  public ResponseEntity<ResponseDto> deleteProduct(@PathVariable Long productId) {
    productServiceImpl.deleteProductById(productId);
    return ResponseEntity.status(HttpStatus.OK)
        .body(new ResponseDto(Constant.STATUS_CODE_OK, Constant.MESSAGE_200));
  }

  @GetMapping("/all")
  public ResponseEntity<List<ProductDto>> getAllProducts() {
    List<ProductDto> products = productServiceImpl.findAllProducts();
    return ResponseEntity.status(HttpStatus.OK).body(products);
  }

  //  GET /api/products/search?name=laptop&page=0&size=5&sortField=price&sortDir=desc
  @GetMapping("/search")
  public List<ProductDto> searchProducts(@RequestParam(required = false) String productName,
                                         @RequestParam(required = false) BigDecimal minPrice,
                                         @RequestParam(required = false) BigDecimal maxPrice,
                                         @RequestParam(defaultValue = "0") int page,
                                         @RequestParam(defaultValue = "10") int size,
                                         @RequestParam(defaultValue = "productId") String sortField,
                                         @RequestParam(defaultValue = "asc") String sortDir) {

    Sort sort = sortDir.equalsIgnoreCase("desc") ? Sort.by(sortField).descending() :
        Sort.by(sortField).ascending();
    Pageable pageable = PageRequest.of(page, size, sort);
    return productServiceImpl.searchProducts(productName, minPrice, maxPrice, pageable);
  }

  @GetMapping("/{productId}/price")
  public BigDecimal getProductPrice(@PathVariable Long productId) {
    return productServiceImpl.getProductPrice(productId);
  }

  @GetMapping("/secure-data")
  public ResponseEntity<String> getData(@RequestHeader("X-User-Roles") String roles) {
    if (roles.contains("ROLE_ADMIN")) {
      return ResponseEntity.ok("Access granted to admin data");
    }
    return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied");
  }


}
