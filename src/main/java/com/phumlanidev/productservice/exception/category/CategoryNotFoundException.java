package com.phumlanidev.productservice.exception.category;



import com.phumlanidev.productservice.exception.BaseException;
import org.springframework.http.HttpStatus;

/**
 * Comment: this is the placeholder for documentation.
 */
public class CategoryNotFoundException extends BaseException {

  /**
   * Comment: this is the placeholder for documentation.
   */
  public CategoryNotFoundException(String message) {
    super(message, HttpStatus.NOT_FOUND);
  }
}
