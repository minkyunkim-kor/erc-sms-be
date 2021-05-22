package com.erclub.sms.api.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SaveBillRequest {
  private List<BillData> billData;
  private String targetMonth;

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  public static class BillData {
    private String studentId;
    private Integer tuition;
    private Integer bookPrice;
    private String comment;
  }
}

