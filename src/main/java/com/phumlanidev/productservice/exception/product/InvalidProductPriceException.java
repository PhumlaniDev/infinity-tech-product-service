package com.phumlanidev.productservice.exception.product;


import com.phumlanidev.productservice.exception.BaseException;
import org.springframework.http.HttpStatus;

public class InvalidProductPriceException extends BaseException {

  public InvalidProductPriceException(String message) {
    super(message, HttpStatus.BAD_REQUEST);
  }
}
