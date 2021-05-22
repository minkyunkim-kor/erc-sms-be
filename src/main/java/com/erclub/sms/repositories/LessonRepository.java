package com.erclub.sms.repositories;

import com.erclub.sms.models.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface LessonRepository extends JpaRepository<Lesson, String> {
  @Query(value = "SELECT DISTINCT l FROM Lesson l left join fetch l.lessonDetails")
  Optional<List<Lesson>> findByUserIdWithDetail(String userId);

  Optional<Lesson> findByUserIdAndLessonId(String userId, String lessonId);

  Optional<Lesson> findByUserIdAndLessonName(String userId, String name);
}
