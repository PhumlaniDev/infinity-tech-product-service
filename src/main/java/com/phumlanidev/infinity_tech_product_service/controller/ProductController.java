package com.phumlanidev.infinity_tech_product_service.controller;


import com.phumlanidev.infinity_tech_product_service.constant.Constant;
import com.phumlanidev.infinity_tech_product_service.dto.ProductDto;
import com.phumlanidev.infinity_tech_product_service.dto.ResponseDto;
import com.phumlanidev.infinity_tech_product_service.service.impl.ProductServiceImpl;
import jakarta.validation.Valid;
import java.math.BigDecimal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
  public ResponseEntity<ResponseDto> createProduct(@Valid @RequestBody ProductDto productDto) {
    productServiceImpl.createProduct(productDto);
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(new ResponseDto(Constant.STATUS_CODE_CREATED, "Product created successfully"));
  }

  /**
   * Comment: this is the placeholder for documentation.
   */
  @PatchMapping("/update/{productId}")
  public ResponseEntity<ProductDto> updateProduct(@Valid @PathVariable Long productId,
                                                  @RequestBody ProductDto productDto) {
    ProductDto updatedProduct = productServiceImpl.updateProduct(productId, productDto);
    return ResponseEntity.ok(updatedProduct);

  }

  /**
   * Comment: this is the placeholder for documentation.
   */
  @GetMapping("/find/{productId}")
  public ResponseEntity<ProductDto> findProductById(@Valid @PathVariable Long productId) {
    ProductDto product = productServiceImpl.findProductById(productId);
    return ResponseEntity.status(HttpStatus.OK).body(product);
  }

  /**
   * Comment: this is the placeholder for documentation.
   */
  @DeleteMapping("/delete/{productId}")
  public ResponseEntity<ResponseDto> deleteProduct(@PathVariable Long productId) {
    productServiceImpl.deleteProductById(productId);
    return ResponseEntity.status(HttpStatus.OK)
        .body(new ResponseDto(Constant.STATUS_CODE_OK, Constant.MESSAGE_200));
  }

  /**
   * Comment: this is the placeholder for documentation.
   */
  @GetMapping
  public ResponseEntity<List<ProductDto>> getAllProducts() {
    List<ProductDto> products = productServiceImpl.findAllProducts();
    return ResponseEntity.status(HttpStatus.OK).body(products);
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

}
