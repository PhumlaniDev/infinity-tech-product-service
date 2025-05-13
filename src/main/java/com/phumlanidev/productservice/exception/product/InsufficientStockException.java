package com.phumlanidev.productservice.exception.product;


import com.phumlanidev.productservice.exception.BaseException;
import org.springframework.http.HttpStatus;

/**
 * Comment: this is the placeholder for documentation.
 */
public class InsufficientStockException extends BaseException {

  /**
   * Comment: this is the placeholder for documentation.
   */
  public InsufficientStockException(String message) {
    super(message, HttpStatus.BAD_REQUEST);
  }
}
