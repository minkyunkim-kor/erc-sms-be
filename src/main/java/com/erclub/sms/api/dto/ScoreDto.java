package com.erclub.sms.api.dto;

import com.erclub.sms.models.Score;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ScoreDto {
  private String studentId;
  private String studentScoreId;
  private String name;
  private String lessonDate;
  private String lessonLevel;
  private Integer scoreA;
  private Integer scoreH;
  private Integer scoreP;
  private Integer scoreM;
  private Integer scoreD;
  private Integer scoreOF;
  private Integer scoreC;
  private Integer scoreG;
  private Integer scoreW;
  private Integer scoreS;
  private String teacher;
  private String comment;

  public ScoreDto(Score score) {
    studentId = score.getStudentId();
    studentScoreId = score.getStudentScoreId();
    name = score.getName();
    lessonDate = score.getTargetDate();
    lessonLevel = score.getLessonLevel();
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
    comment = score.getExtra();
  }
}
