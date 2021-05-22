package com.erclub.sms.api.dto;

import com.erclub.sms.models.CashReceipt;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CashReceiptDto {
  private String studentId;
  private String name;
  private String grade;
  private String cashReceipt;
  private String comment;

  public CashReceiptDto(CashReceipt cashReceipt) {
    this.studentId = cashReceipt.getStudentId();
    this.name = cashReceipt.getName();
    this.grade = cashReceipt.getGrade();
    this.cashReceipt = cashReceipt.getCashReceiptInfo();
    this.comment = cashReceipt.getExtra();
  }
}
