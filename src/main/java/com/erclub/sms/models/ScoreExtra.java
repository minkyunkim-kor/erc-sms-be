package com.erclub.sms.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "ERC_STUDENT_SCR_EXTRA")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScoreExtra {

  @Id
  @Column(name = "STUDENT_ID")
  private String studentId;

  @Column(name = "TARGET_DATE")
  private String targetDate;

  @Column(name = "LAST_LESSON_LEVEL")
  private String lastLessonLevel;

}
