package com.erclub.sms.api.dto;

import com.erclub.sms.common.domain.STUDENT_CATEGORY;
import com.erclub.sms.models.Student;
import lombok.Data;
import lombok.NoArgsConstructor;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Data
@NoArgsConstructor
public class StudentDto {
  private String studentId;
  private String category;
  private String registeredDate;
  private String nameKorean;
  private String nameEnglish;
  private String grade;
  private String gender;
  private String school;
  private String contact;
  private String address;
  private String lessonName;
  private String extra;

  public StudentDto(Student student) {
    studentId = student.getStudentId();
    category = STUDENT_CATEGORY.convertByValue(student.getCategory()).getName();
    registeredDate = student.getRegisteredDate();
    nameKorean = student.getNameKo();
    nameEnglish = nvl(student.getNameEn());
    grade = nvl(student.getGrade());
    gender = nvl(student.getGender());
    school = nvl(student.getSchool());
    contact = nvl(student.getContact());
    address = nvl(student.getAddr());
    lessonName = null != student.getLesson() ? nvl(student.getLesson().getLessonName()) : "";
    extra = nvl(student.getExtra());
  }

  private String nvl(String value) {
    return isNotBlank(value) ? value : "";
  }
}
