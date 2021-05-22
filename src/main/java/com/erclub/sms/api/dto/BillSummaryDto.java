package com.erclub.sms.api.dto;

import com.erclub.sms.services.vo.BillSummaryVo;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BillSummaryDto {
  private Integer month;
  private Integer tuition;
  private Integer bookPrice;
  private Integer deposit;
  private Integer card;
  private Integer cash;

  public BillSummaryDto(BillSummaryVo vo) {
    month = vo.getMonth();
    tuition = vo.getTuition();
    bookPrice = vo.getBookPrice();
    deposit = vo.getDeposit();
    card = vo.getCard();
    cash = vo.getCash();
  }
}
