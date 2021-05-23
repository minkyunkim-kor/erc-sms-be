package com.erclub.sms.api.dto;

import com.erclub.sms.models.ErcUser;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
public class SignInDto {
  private String token;
  private String uid;
  private String role;
  private String name;

  public SignInDto(ErcUser ercUser) {
    token = ercUser.getToken();
    uid = ercUser.getUserId();
    role = ercUser.getRole();
    name = ercUser.getName();
  }
}
