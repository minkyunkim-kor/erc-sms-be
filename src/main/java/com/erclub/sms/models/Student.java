package com.erclub.sms.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.LazyToOne;
import org.hibernate.annotations.LazyToOneOption;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ERC_STUDENT")
@Data
@NoArgsConstructor
@ToString()
public class Student {

  @Id
  @GeneratedValue(generator = "uuid")
  @GenericGenerator(name = "uuid", strategy = "uuid2")
  @Column(name = "STUDENT_ID")
  private String studentId;

  @Column(name = "USER_ID")
  private String userId;

  @Column(name = "CATEGORY")
  private Integer category;

  @Column(name = "REGISTERED_DATE")
  private String registeredDate;

  @Column(name = "NAME_KO")
  private String nameKo;

  @Column(name = "NAME_EN")
  private String nameEn;

  @Column(name = "GRADE")
  private String grade;

  @Column(name = "GENDER")
  private String gender;

  @Column(name = "SCHOOL")
  private String school;

  @Column(name = "CONTACT")
  private String contact;

  @Column(name = "ADDR")
  private String addr;

  @Column(name = "EXTRA")
  private String extra;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "LESSON_ID")
  private Lesson lesson;

  protected Student(String userId, Integer category, String registeredDate, String nameKo, String nameEn, String grade, String gender, String school, String contact, String addr, String extra, Lesson lesson) {
    this.userId = userId;
    this.category = category;
    this.registeredDate = registeredDate;
    this.nameKo = nameKo;
    this.nameEn = nameEn;
    this.grade = grade;
    this.gender = gender;
    this.school = school;
    this.contact = contact;
    this.addr = addr;
    this.extra = extra;
    this.lesson = lesson;
  }

  public static Student from(String userId, Integer category, String registeredDate, String nameKo, String nameEn, String grade, String gender, String school, String contact, String addr, String extra, Lesson lesson) {
    return new Student(userId, category, registeredDate, nameKo, nameEn, grade, gender, school, contact, addr, extra, lesson);
  }
}
