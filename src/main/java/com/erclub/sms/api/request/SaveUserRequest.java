package com.erclub.sms.api.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SaveUserRequest {
  private String id;
  private String pw;
  private String teacher;
  private String name;
  private String suspendYn;
  private Boolean isNew;
}
