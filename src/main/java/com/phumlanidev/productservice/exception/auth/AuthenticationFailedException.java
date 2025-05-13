package com.phumlanidev.productservice.exception.auth;

import com.phumlanidev.productservice.exception.BaseException;
import org.springframework.http.HttpStatus;

/**
 * Comment: this is the placeholder for documentation.
 */
public class AuthenticationFailedException extends BaseException {

  /**
   * Comment: this is the placeholder for documentation.
   */
  public AuthenticationFailedException(String message) {
    super(message, HttpStatus.UNAUTHORIZED);
  }
}
