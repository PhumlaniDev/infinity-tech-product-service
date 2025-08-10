package com.phumlanidev.productservice.exception.category;


import com.phumlanidev.productservice.exception.BaseException;
import org.springframework.http.HttpStatus;

public class CategoryAlreadyExistsException extends BaseException {

  public CategoryAlreadyExistsException(String message) {
    super(message, HttpStatus.BAD_REQUEST);
  }
}
