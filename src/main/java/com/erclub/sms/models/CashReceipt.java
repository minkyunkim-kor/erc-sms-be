package com.erclub.sms.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "ERC_STUDENT_BILL_CASH_RECEIPT")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CashReceipt {

  @Id
  @Column(name = "STUDENT_ID")
  private String studentId;

  @Column(name = "CASH_RECEIPT_INFO")
  private String cashReceiptInfo;

  @Column(name = "EXTRA")
  private String extra;

  @Transient
  private String name;

  @Transient
  private String grade;

  protected CashReceipt(String studentId) {
    this.studentId = studentId;
  }

  public static CashReceipt from(String studentId) {
    return new CashReceipt(studentId);
  }
}
