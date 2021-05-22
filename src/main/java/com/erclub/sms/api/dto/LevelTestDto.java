package com.erclub.sms.api.dto;

import com.erclub.sms.models.LevelTest;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LevelTestDto {
  private String studentId;
  private String nameKorean;
  private String nameEnglish;
  private String grade;
  private String testLevel;
  private Integer testScore;
  private String initLevelA;
  private String initLevelB;
  private String memo;

  public LevelTestDto(LevelTest levelTest) {
    studentId = levelTest.getStudentId();
    nameKorean = levelTest.getNameKorean();
    nameEnglish = levelTest.getNameEnglish();
    grade = levelTest.getGrade();
    testLevel = levelTest.getInitTestLevel();
    testScore = levelTest.getInitTestScore();
    initLevelA = levelTest.getInitLevelA();
    initLevelB = levelTest.getInitLevelB();
    memo = levelTest.getExtra();
  }
}
