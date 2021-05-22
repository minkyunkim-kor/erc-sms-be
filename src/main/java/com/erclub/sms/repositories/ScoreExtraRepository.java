package com.erclub.sms.repositories;

import com.erclub.sms.models.ScoreExtra;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ScoreExtraRepository extends JpaRepository<ScoreExtra, String> {
  Optional<List<ScoreExtra>> findByStudentIdIn(List<String> studentIds);

  @Modifying
  @Query(value = "UPDATE ScoreExtra s SET targetDate = ?3, lastLessonLevel = ?2 WHERE studentId = ?1 AND targetDate <= ?3")
  void update(String studentId, String level, String date);
}
