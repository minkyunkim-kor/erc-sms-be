package com.erclub.sms.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "ERC_STUDENT_SCR")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Score implements Cloneable {

  @Id
  @GeneratedValue(generator = "uuid")
  @GenericGenerator(name = "uuid", strategy = "uuid2")
  @Column(name = "STUDENT_SCR_ID")
  private String studentScoreId;

  @Column(name = "STUDENT_ID")
  private String studentId;

  @Column(name = "TARGET_DATE")
  private String targetDate;

  @Column(name = "LESSON_LEVEL")
  private String lessonLevel;

  @Column(name = "SCORE_A")
  private Integer scoreA;

  @Column(name = "SCORE_H")
  private Integer scoreH;

  @Column(name = "SCORE_P")
  private Integer scoreP;

  @Column(name = "SCORE_M")
  private Integer scoreM;

  @Column(name = "SCORE_D")
  private Integer scoreD;

  @Column(name = "SCORE_OF")
  private Integer scoreOF;

  @Column(name = "SCORE_C")
  private Integer scoreC;

  @Column(name = "SCORE_G")
  private Integer scoreG;

  @Column(name = "SCORE_W")
  private Integer scoreW;

  @Column(name = "SCORE_S")
  private Integer scoreS;

  @Column(name = "ABSENT")
  private Boolean absent;

  @Column(name = "TEACHER")
  private String teacher;

  @Column(name = "EXTRA")
  private String extra;

  @Column(name = "COMPLETED")
  private Boolean completed;

  @Transient
  private String name;

  @Transient
  private String nameEn;

  @Transient
  private String lastLevel;

  public void setScoreValue(Score score) {
    lessonLevel = score.getLessonLevel();
    absent = score.getAbsent();
    scoreA = score.getScoreA();
    scoreH = score.getScoreH();
    scoreP = score.getScoreP();
    scoreM = score.getScoreM();
    scoreD = score.getScoreD();
    scoreOF = score.getScoreOF();
    scoreC = score.getScoreC();
    scoreG = score.getScoreG();
    scoreW = score.getScoreW();
    scoreS = score.getScoreS();
    teacher = score.getTeacher();
    extra = score.getExtra();
  }

  protected Score(String studentId) {
    this.studentId = studentId;
  }

  public static Score from(String studentId) {
    return new Score(studentId);
  }
}
