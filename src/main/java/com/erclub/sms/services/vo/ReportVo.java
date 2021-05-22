package com.erclub.sms.services.vo;

import com.erclub.sms.models.Student;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ReportVo {
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

  protected ReportVo(String nameKorean, String nameEnglish, String grade, String school, String registeredDate) {
    this.nameKorean = nameKorean;
    this.nameEnglish = nameEnglish;
    this.grade = grade;
    this.school = school;
    this.registeredDate = registeredDate;
  }

  public static ReportVo from(Student student) {
    return new ReportVo(student.getNameKo(), student.getNameEn(), student.getGrade(), student.getSchool(), student.getRegisteredDate());
  }
}
