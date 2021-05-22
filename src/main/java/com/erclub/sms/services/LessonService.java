package com.erclub.sms.services;

import com.erclub.sms.api.request.SaveLessonRequest;
import com.erclub.sms.common.domain.WEEKDAY;
import com.erclub.sms.common.exception.CommonException;
import com.erclub.sms.models.Lesson;
import com.erclub.sms.models.LessonDetail;
import com.erclub.sms.repositories.LessonDetailRepository;
import com.erclub.sms.repositories.LessonRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
public class LessonService {
  private final LessonRepository lessonRepository;
  private final LessonDetailRepository lessonDetailRepository;

  public LessonService(LessonRepository lessonRepository, LessonDetailRepository lessonDetailRepository) {
    this.lessonRepository = lessonRepository;
    this.lessonDetailRepository = lessonDetailRepository;
  }

  @Transactional(readOnly = true)
  public List<Lesson> getLessons(String userId) {
    return lessonRepository.findByUserIdWithDetail(userId)
            .orElseThrow(() ->
              new CommonException(NOT_FOUND.value(), "not found lesson info")
            );
  }

  @Transactional(readOnly = true)
  public List<Lesson> getLessons(String userId, String targetDate) {
    List<Lesson> lessons = getLessons(userId);
    int dayOfWeek = LocalDate.parse(targetDate).getDayOfWeek().getValue() - 1;
    List<Lesson> result = new ArrayList<>();
    for (Lesson lesson : lessons) {
      for (LessonDetail lessonDetail : lesson.getLessonDetails()) {
        if (lessonDetail.getWeekday() == dayOfWeek) {
          result.add(lesson);
          break;
        }
      }
    }
    return result;
  }

  @Transactional(readOnly = true)
  public Lesson getLesson(String userId, String lessonId) {
    return getLessons(userId)
        .stream()
        .filter(lesson -> lesson.getLessonId().equals(lessonId))
        .findFirst()
        .orElseThrow(() ->
            new CommonException(NOT_FOUND.value(), "not found lesson info")
        );
  }

  @Transactional
  public void saveLesson(String userId, SaveLessonRequest request) {
    if (lessonRepository.findByUserIdAndLessonName(userId, request.getName()).isPresent()) {
      throw new CommonException(BAD_REQUEST.value(), "duplicate lesson name");
    }

    Lesson lesson = Lesson.from(userId, request.getName(), request.getTeacherName());
    addDetails(lesson, request.getTimes());

    lessonRepository.save(lesson);
  }

  @Transactional
  public void updateLesson(String userId, String lessonId, SaveLessonRequest request) {
    Optional<Lesson> lesson = lessonRepository.findByUserIdAndLessonName(userId, request.getName());
    if (!lesson.isPresent()) {
      throw new CommonException(BAD_REQUEST.value(), "not found lesson info");
    }

    if (!lessonId.equals(lesson.get().getLessonId())) {
      throw new CommonException(HttpStatus.BAD_REQUEST.value(), "duplicate lesson name");
    }

    lessonDetailRepository.deleteAll(lesson.get().getLessonDetails());

    lesson.get().setLessonName(request.getName());
    lesson.get().setTeacherName(request.getTeacherName());
    lesson.get().setLessonDetails(new ArrayList<>());
    addDetails(lesson.get(), request.getTimes());

    lessonRepository.save(lesson.get());
  }

  @Transactional
  public void deleteLesson(String userId, String lessonId) {
    Optional<Lesson> lesson = lessonRepository.findByUserIdAndLessonId(userId, lessonId);
    if (!lesson.isPresent()) {
      throw new CommonException(BAD_REQUEST.value(), "not found lesson info");
    }
    lessonRepository.delete(lesson.get());
  }

  private void addDetails(Lesson lesson, List<SaveLessonRequest.Time> times) {
    for (SaveLessonRequest.Time time : times) {
      WEEKDAY weekday = WEEKDAY.convertBySource(time.getWeekday());
      if (null == weekday) {
        throw new CommonException(HttpStatus.BAD_REQUEST.value(), "invalid weekday value");
      }
      lesson.addDetails(LessonDetail.from(weekday.getTarget(), time.getStart(), time.getEnd()));
    }
  }
}
