package com.erclub.sms.api.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SaveLevelTestRequest {
  private String initTestLevel;
  private Integer initTestScore;
  private String initLevelA;
  private String initLevelB;
  private String memo;
}
