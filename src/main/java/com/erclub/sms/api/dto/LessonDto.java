package com.erclub.sms.api.dto;

import com.erclub.sms.common.domain.WEEKDAY;
import com.erclub.sms.models.Lesson;
import com.erclub.sms.models.LessonDetail;
import com.erclub.sms.models.Student;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public class LessonDto {
  private String lessonId;
  private String name;
  private String teacher;
  private String student;
  private Integer studentCount;
  private List<LessonTime> times;

  public LessonDto(Lesson lesson) {
    List<String> students = lesson.getStudents()
            .stream()
            .map(Student::getNameKo)
            .collect(Collectors.toList());
    lessonId = lesson.getLessonId();
    name = lesson.getLessonName();
    teacher = lesson.getTeacherName();
    student = StringUtils.join(students, ", ");
    studentCount = students.size();
    times = lesson.getLessonDetails()
        .stream()
        .map(LessonTime::new)
        .sorted()
        .collect(Collectors.toList());
  }

  @Data
  @NoArgsConstructor
  public static class LessonTime implements Comparable<LessonTime> {
    private String weekday;
    private String start;
    private String end;

    public LessonTime(LessonDetail detail) {
      weekday = Objects.requireNonNull(WEEKDAY.convertByTarget(detail.getWeekday())).getSource();
      start = detail.getStartTs();
      end = detail.getEndTs();
    }

    @Override
    public int compareTo(LessonTime o) {
      return Integer.compare(
          Objects.requireNonNull(WEEKDAY.convertBySource(weekday)).getTarget(),
          Objects.requireNonNull(WEEKDAY.convertBySource(o.getWeekday())).getTarget()
      );
    }
  }
}
