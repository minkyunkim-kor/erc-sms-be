package com.erclub.sms.api.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BillPaymentRequest {
  private String studentId;
  private String targetMonth;
  private Integer deposit;
  private String depositDate;
  private String depositMethod;
  private String cashReceipt;
  private String comment;
}