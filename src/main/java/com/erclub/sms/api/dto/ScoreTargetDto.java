package com.erclub.sms.api.dto;

import com.erclub.sms.models.Score;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ScoreTargetDto {
  private String studentId;
  private String studentName;
  private String studentNameEn;
  private String lastLevel;
  private Boolean absent;
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
  private boolean completed;

  public ScoreTargetDto(Score score) {
    studentId = score.getStudentId();
    studentName = score.getName();
    studentNameEn = score.getNameEn();
    lastLevel = score.getLessonLevel();
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
    comment = score.getExtra();
    completed = null != score.getCompleted() ? score.getCompleted() : false;
  }
}
