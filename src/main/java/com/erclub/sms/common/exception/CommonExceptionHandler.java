package com.erclub.sms.common.exception;

import com.erclub.sms.common.domain.CommonResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class CommonExceptionHandler {

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<CommonResponse> handleIllegalArgumentException(IllegalArgumentException e) {
    return new ResponseEntity<>(CommonResponse.of(e.getMessage()), HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(UsernameNotFoundException.class)
  public ResponseEntity<CommonResponse> handleUsernameNotFoundException(UsernameNotFoundException e) {
    return new ResponseEntity<>(CommonResponse.of("not exist user"), HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(BadCredentialsException.class)
  public ResponseEntity<CommonResponse> handleBadCredentialsException(BadCredentialsException e) {
    return new ResponseEntity<>(CommonResponse.of("invalid password"), HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<CommonResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
    return new ResponseEntity<>(CommonResponse.of("not null field: " + e.getBindingResult().getFieldErrors().get(0).getField()), HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(CommonException.class)
  public ResponseEntity<CommonResponse> handleCommonException(CommonException e) {
    return new ResponseEntity<>(CommonResponse.of(e.getMessage()), HttpStatus.valueOf(e.getStatus()));
  }
}
