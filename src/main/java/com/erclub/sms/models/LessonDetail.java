package com.erclub.sms.models;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "ERC_LESSON_DTL")
@Data
@NoArgsConstructor
@ToString
public class LessonDetail {

  @Id
  @GeneratedValue(generator = "uuid")
  @GenericGenerator(name = "uuid", strategy = "uuid2")
  @Column(name = "LESSON_DTL_ID")
  private String lessonDetailId;

  @Column(name = "WEEKDAY")
  private int weekday;

  @Column(name = "START_TS")
  private String startTs;

  @Column(name = "END_TS")
  private String endTs;

  @ManyToOne
  @JoinColumn(name = "LESSON_ID", updatable = false)
  private Lesson lesson;

  protected LessonDetail(int weekday, String startTs, String endTs) {
    this.weekday = weekday;
    this.startTs = startTs;
    this.endTs = endTs;
  }

  public static LessonDetail from(int weekday, String startTs, String endTs) {
    return new LessonDetail(weekday, startTs, endTs);
  }
}