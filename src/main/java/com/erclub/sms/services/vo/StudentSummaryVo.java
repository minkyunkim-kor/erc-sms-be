package com.erclub.sms.services.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
public class StudentSummaryVo {

  private Integer total;
  private CategorySummary category;
  private Map<String, Integer> register;

  @Data
  @NoArgsConstructor
  @AllArgsConstructor(staticName = "of")
  public static class CategorySummary {
    private Integer newly;
    private Integer registered;
    private Integer rest;
    private Integer withdrawal;
  }
}
