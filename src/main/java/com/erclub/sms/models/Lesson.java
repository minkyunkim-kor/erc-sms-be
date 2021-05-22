package com.erclub.sms.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name="ERC_LESSON")
@Data
@NoArgsConstructor
@ToString
public class Lesson implements Comparable<Lesson> {

  @Id
  @GeneratedValue(generator = "uuid")
  @GenericGenerator(name = "uuid", strategy = "uuid2")
  @Column(name = "LESSON_ID")
  private String lessonId;

  @Column(name = "USER_ID")
  private String userId;

  @Column(name = "LESSON_NM")
  private String lessonName;

  @Column(name = "TEACHER_NM")
  private String teacherName;

  @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "lesson")
  private List<LessonDetail> lessonDetails = new ArrayList<>();

  @OneToMany(fetch = FetchType.LAZY, mappedBy = "lesson")
  private List<Student> students = new ArrayList<>();

  public void addDetails(LessonDetail detail) {
    detail.setLesson(this);
    lessonDetails.add(detail);
  }

  protected Lesson(String userId, String lessonName, String teacherName) {
    this.userId = userId;
    this.lessonName = lessonName;
    this.teacherName = teacherName;
  }

  public static Lesson from(String userId, String lessonName, String teacherName) {
    return new Lesson(userId, lessonName, teacherName);
  }

  @Override
  public int compareTo(Lesson o) {
    return lessonName.compareTo(o.getLessonName());
  }
}
