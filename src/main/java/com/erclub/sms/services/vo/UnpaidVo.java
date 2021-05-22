package com.erclub.sms.services.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UnpaidVo {

  private String name;
  private String grade;
  private int total;
  private int january;
  private int february;
  private int march;
  private int april;
  private int may;
  private int june;
  private int july;
  private int august;
  private int september;
  private int october;
  private int november;
  private int december;

  protected UnpaidVo(String name, String grade) {
    this.name = name;
    this.grade = grade;
  }

  public static UnpaidVo from(String name, String grade) {
    return new UnpaidVo(name, grade);
  }

  public void setUnpaid(int month, int unpaid) {
    this.total += unpaid;
    switch (month) {
      case 1:
        this.setJanuary(unpaid);
        break;
      case 2:
        this.setFebruary(unpaid);
        break;
      case 3:
        this.setMarch(unpaid);
        break;
      case 4:
        this.setApril(unpaid);
        break;
      case 5:
        this.setMay(unpaid);
        break;
      case 6:
        this.setJune(unpaid);
        break;
      case 7:
        this.setJuly(unpaid);
        break;
      case 8:
        this.setAugust(unpaid);
        break;
      case 9:
        this.setSeptember(unpaid);
        break;
      case 10:
        this.setOctober(unpaid);
        break;
      case 11:
        this.setNovember(unpaid);
        break;
      case 12:
        this.setDecember(unpaid);
        break;
    }
  }
}
