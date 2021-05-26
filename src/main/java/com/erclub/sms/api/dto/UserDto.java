package com.erclub.sms.api.dto;

import com.erclub.sms.models.ErcUser;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
  private String userId;
  private String id;
  private String teacher;
  private String name;
  private String suspendYn;

  public UserDto(ErcUser user) {
    this(user.getUserId(), user.getLoginId(), user.getTeacher(), user.getName(), user.getSuspendYn());
  }
}
