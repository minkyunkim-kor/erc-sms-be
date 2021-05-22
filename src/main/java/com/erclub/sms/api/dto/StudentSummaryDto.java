package com.erclub.sms.api.dto;

import com.erclub.sms.services.vo.StudentSummaryVo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
public class StudentSummaryDto {
  private Integer total;
  private Category category;
  private Map<String, Integer> register;

  public StudentSummaryDto(StudentSummaryVo vo) {
    total = vo.getTotal();
    category = new Category(vo.getCategory());
    register = vo.getRegister();
  }

  @Data
  @NoArgsConstructor
  public static class Category {
    private Integer newly;
    private Integer register;
    private Integer rest;
    private Integer withdrawal;

    public Category(StudentSummaryVo.CategorySummary categorySummary) {
      newly = categorySummary.getNewly();
      register = categorySummary.getRegistered();
      rest = categorySummary.getRest();
      withdrawal = categorySummary.getWithdrawal();
    }
  }
}
