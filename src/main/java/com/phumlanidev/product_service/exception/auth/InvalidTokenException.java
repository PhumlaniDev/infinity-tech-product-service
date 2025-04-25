package com.phumlanidev.product_service.exception.auth;


import com.phumlanidev.product_service.exception.BaseException;
import org.springframework.http.HttpStatus;

/**
 * Comment: this is the placeholder for documentation.
 */
public class InvalidTokenException extends BaseException {

  /**
   * Comment: this is the placeholder for documentation.
   */
  public InvalidTokenException(String message) {
    super(message, HttpStatus.UNAUTHORIZED);
  }
}
