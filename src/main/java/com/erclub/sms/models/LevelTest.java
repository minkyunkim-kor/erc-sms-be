package com.erclub.sms.models;

import com.erclub.sms.api.request.SaveLevelTestRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "ERC_STUDENT_SCR_LVL_TEST")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LevelTest {

  @Id
  @Column(name = "STUDENT_ID")
  private String studentId;

  @Column(name = "INIT_TEST_LVL")
  private String initTestLevel;

  @Column(name = "INIT_TEST_SCORE")
  private Integer initTestScore;

  @Column(name = "INIT_LVL_A")
  private String initLevelA;

  @Column(name = "INIT_LVL_B")
  private String initLevelB;

  @Column(name = "EXTRA")
  private String extra;

  @Transient
  private String nameKorean;

  @Transient
  private String nameEnglish;

  @Transient
  private String grade;

  public void addStudentInfo(Student student) {
    this.nameKorean = student.getNameKo();
    this.nameEnglish = student.getNameEn();
    this.grade = student.getGrade();
  }

  protected LevelTest(String studentId) {
    this.studentId = studentId;
  }

  protected LevelTest(String studentId, SaveLevelTestRequest request) {
    this.studentId = studentId;
    initTestLevel = request.getInitTestLevel();
    initTestScore = request.getInitTestScore();
    initLevelA = request.getInitLevelA();
    initLevelB = request.getInitLevelB();
    extra = request.getMemo();
  }

  public static LevelTest from(String studentId) {
    return new LevelTest(studentId);
  }

  public static LevelTest from(String studentId, SaveLevelTestRequest request) {
    return new LevelTest(studentId, request);
  }
}
