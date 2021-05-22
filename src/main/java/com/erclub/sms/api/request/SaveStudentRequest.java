package com.erclub.sms.api.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import static com.google.common.base.Preconditions.checkArgument;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
public class SaveStudentRequest {
  private String category;
  private String registeredDate;
  private String nameKorean;
  private String nameEnglish;
  private String grade;
  private String gender;
  private String school;
  private String contact;
  private String address;
  private String lessonName;
  private String extra;

  public void validate() {
    checkArgument(isNotBlank(nameKorean), "not null field: 이름");
  }
}
