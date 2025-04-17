package com.phumlanidev.infinity_tech_product_service.exception.product;


import com.phumlanidev.infinity_tech_product_service.exception.BaseException;
import org.springframework.http.HttpStatus;

/**
 * Comment: this is the placeholder for documentation.
 */
public class InvalidProductPriceException extends BaseException {

  /**
   * Comment: this is the placeholder for documentation.
   */
  public InvalidProductPriceException(String message) {
    super(message, HttpStatus.BAD_REQUEST);
  }
}
