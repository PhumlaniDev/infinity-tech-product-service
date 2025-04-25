package com.phumlanidev.product_service.exception.auth;

import com.phumlanidev.product_service.exception.BaseException;
import org.springframework.http.HttpStatus;

/**
 * Comment: this is the placeholder for documentation.
 */
public class MissingRoleException extends BaseException {

  /**
   * Comment: this is the placeholder for documentation.
   */
  public MissingRoleException(String message) {
    super(message, HttpStatus.FORBIDDEN);
  }
}
