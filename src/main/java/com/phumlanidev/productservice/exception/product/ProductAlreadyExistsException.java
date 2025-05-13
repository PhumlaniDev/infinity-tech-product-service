package com.phumlanidev.productservice.exception.product;

import com.phumlanidev.productservice.exception.BaseException;
import org.springframework.http.HttpStatus;

/**
 * Comment: this is the placeholder for documentation.
 */
public class ProductAlreadyExistsException extends BaseException {

  /**
   * Comment: this is the placeholder for documentation.
   */
  public ProductAlreadyExistsException(String message) {
    super(message, HttpStatus.CONFLICT);
  }
}
