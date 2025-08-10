package com.phumlanidev.productservice.exception.product;


import com.phumlanidev.productservice.exception.BaseException;
import org.springframework.http.HttpStatus;

public class InsufficientStockException extends BaseException {

  public InsufficientStockException(String message) {
    super(message, HttpStatus.BAD_REQUEST);
  }
}
