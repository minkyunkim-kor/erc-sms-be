package com.erclub.sms.models;

import com.erclub.sms.api.request.SaveUserRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name="ERC_USER")
@Data
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
public class ErcUser implements Serializable {

  @Id
  @Column(name = "USER_ID", nullable = false, unique = true)
  private String userId;

  @Column(name = "ID", nullable = false)
  private String loginId;

  @Column(name = "PWD", nullable = false)
  private String pwd;

  @Column(name = "TEACHER")
  private String teacher;

  @Column(name = "NAME")
  private String name;

  @Column(name = "ROLE")
  private String role;

  @Column(name = "SUSPEND_YN")
  private String suspendYn;

  @Transient
  private String token;

  protected ErcUser(String id, String pwd, String teacher, String name, String suspendYn) {
    userId = UUID.randomUUID().toString();
    this.loginId = id;
    this.pwd = pwd;
    this.teacher = teacher;
    this.name = name;
    role = "USER";
    this.suspendYn = suspendYn;
  }

  public static ErcUser from(SaveUserRequest request) {
    return new ErcUser(request.getId(), request.getPw(), request.getTeacher(), request.getName(), request.getSuspendYn());
  }
}
