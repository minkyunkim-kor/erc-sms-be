package com.erclub.sms.repositories;

import com.erclub.sms.models.Score;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ScoreRepository extends JpaRepository<Score, String> {
  Optional<List<Score>> findByStudentIdInAndTargetDateBetween(List<String> studentId, String start, String end);

  Optional<List<Score>> findByStudentIdInAndTargetDate(List<String> studentId, String targetDate);

  Optional<Score> findByStudentIdAndTargetDate(String studentId, String targetDate);
}
