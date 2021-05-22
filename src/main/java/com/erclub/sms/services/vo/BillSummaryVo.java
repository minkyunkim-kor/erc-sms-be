package com.erclub.sms.services.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BillSummaryVo {
  private Integer month;
  private Integer tuition;
  private Integer bookPrice;
  private Integer deposit;
  private Integer card;
  private Integer cash;

  protected BillSummaryVo(Integer month) {
    this.month = month;
    tuition = 0;
    bookPrice = 0;
    deposit = 0;
    card = 0;
    cash = 0;
  }

  public static BillSummaryVo from(Integer month) {
    return new BillSummaryVo(month);
  }
}
