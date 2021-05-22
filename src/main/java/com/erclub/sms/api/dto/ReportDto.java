package com.erclub.sms.api.dto;

import com.erclub.sms.services.vo.ReportVo;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ReportDto {
  private String nameKorean;
  private String nameEnglish;
  private String grade;
  private String level;
  private String school;
  private String registeredDate;
  private Double averageA;
  private Double averageH;
  private Double averageP;
  private Double averageM;
  private Double averageD;
  private Double averageOF;
  private Double averageC;
  private Double averageG;
  private Double averageW;
  private Double averageS;

  public ReportDto(ReportVo vo) {
    nameKorean = vo.getNameKorean();
    nameEnglish = vo.getNameEnglish();
    grade = vo.getGrade();
    level = vo.getLevel();
    school = vo.getSchool();
    registeredDate = vo.getRegisteredDate();
    averageA = vo.getAverageA();
    averageH = vo.getAverageH();
    averageP = vo.getAverageP();
    averageM = vo.getAverageM();
    averageD = vo.getAverageD();
    averageOF = vo.getAverageOF();
    averageC = vo.getAverageC();
    averageG = vo.getAverageG();
    averageW = vo.getAverageW();
    averageS = vo.getAverageS();
  }
}
