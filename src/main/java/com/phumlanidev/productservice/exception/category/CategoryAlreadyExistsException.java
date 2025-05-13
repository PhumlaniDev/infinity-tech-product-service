package com.phumlanidev.productservice.exception.category;


import com.phumlanidev.productservice.exception.BaseException;
import org.springframework.http.HttpStatus;

/**
 * Comment: this is the placeholder for documentation.
 */
public class CategoryAlreadyExistsException extends BaseException {

  /**
   * Comment: this is the placeholder for documentation.
   */
  public CategoryAlreadyExistsException(String message) {
    super(message, HttpStatus.BAD_REQUEST);
  }
}
