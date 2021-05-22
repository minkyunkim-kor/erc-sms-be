package com.erclub.sms.models;

import com.erclub.sms.common.domain.STUDENT_CATEGORY;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Entity
@Table(name = "ERC_STUDENT_PROGRESS")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Progress {
  @Id
  @Column(name = "STUDENT_ID")
  private String studentId;

  @Column(name = "CURRENT_LEVEL")
  private String currentLevel;

  @Column(name = "PROGRESS")
  private String progress;

  @Transient
  private STUDENT_CATEGORY category;

  @Transient
  private String name;

  @Transient
  private String nameEn;

  @Transient
  private List<String> progresses;

  protected Progress(String studentId) {
    this.studentId = studentId;
    this.currentLevel = "-";
  }

  public static Progress from(String studentId) {
    return new Progress(studentId);
  }

  public void setStudent(Student student) {
    name = student.getNameKo();
    nameEn = student.getNameEn();
    category = STUDENT_CATEGORY.convertByValue(student.getCategory());
  }

  public void convertProgress() {
    if (StringUtils.isEmpty(progress)) {
      progresses = new ArrayList<>();
    } else if(progress.contains(",")) {
      progresses = Arrays.asList(progress.split(","));
    } else {
      progresses = Collections.singletonList(progress);
    }
  }
}
