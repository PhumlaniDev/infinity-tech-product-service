package com.phumlanidev.productservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class UserNotFoundException extends BaseException {

  public UserNotFoundException(String message) {
    super(message, HttpStatus.NOT_FOUND);
  }

}
