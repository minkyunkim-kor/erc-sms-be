package com.erclub.sms.api.dto;

import com.erclub.sms.models.Bill;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BillDto {
  private String studentId;
  private String name;
  private String grade;
  private Integer category;
  private Integer tuition;
  private Integer bookPrice;
  private Integer deposit;
  private String depositDate;
  private String depositMethod;
  private String comment;

  public BillDto(Bill bill) {
    studentId = bill.getStudentId();
    name = bill.getName();
    grade = bill.getGrade();
    category = bill.getCategory();
    tuition = bill.getTuition();
    bookPrice = bill.getBookPrice();
    deposit = bill.getDeposit();
    depositDate = bill.getDepositDate();
    depositMethod = bill.getDepositMethod();
    comment = bill.getExtra();
  }
}
