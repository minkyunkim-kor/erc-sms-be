package com.erclub.sms.common.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommonException extends RuntimeException {
  private int status;
  private String message;
}
