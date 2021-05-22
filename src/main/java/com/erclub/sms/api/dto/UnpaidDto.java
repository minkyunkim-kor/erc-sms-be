package com.erclub.sms.api.dto;

import com.erclub.sms.services.vo.UnpaidVo;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UnpaidDto {
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

  public UnpaidDto(UnpaidVo vo) {
    name = vo.getName();
    grade = vo.getGrade();
    total = vo.getTotal();
    january = vo.getJanuary();
    february = vo.getFebruary();
    march = vo.getMarch();
    april = vo.getApril();
    may = vo.getMay();
    june = vo.getJune();
    july = vo.getJuly();
    august = vo.getAugust();
    september = vo.getSeptember();
    october = vo.getOctober();
    november = vo.getNovember();
    december = vo.getDecember();
  }
}
