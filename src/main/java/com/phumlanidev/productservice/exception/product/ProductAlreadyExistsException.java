package com.phumlanidev.productservice.exception.product;

import com.phumlanidev.productservice.exception.BaseException;
import org.springframework.http.HttpStatus;

public class ProductAlreadyExistsException extends BaseException {

  public ProductAlreadyExistsException(String message) {
    super(message, HttpStatus.CONFLICT);
  }
}
