package com.phumlanidev.productservice.exception.category;



import com.phumlanidev.productservice.exception.BaseException;
import org.springframework.http.HttpStatus;

public class CategoryNotFoundException extends BaseException {

  public CategoryNotFoundException(String message) {
    super(message, HttpStatus.NOT_FOUND);
  }
}
