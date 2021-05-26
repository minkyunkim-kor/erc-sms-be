package com.erclub.sms.repositories;

import com.erclub.sms.models.LastLevel;
import com.erclub.sms.models.Score;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ScoreRepository extends JpaRepository<Score, String> {
  Optional<List<Score>> findByStudentIdInAndTargetDateBetween(List<String> studentId, String start, String end);

  Optional<List<Score>> findByStudentIdInAndTargetDate(List<String> studentId, String targetDate);

  Optional<Score> findByStudentIdAndTargetDate(String studentId, String targetDate);

  @Query(value = "SELECT O.STUDENT_ID as StudentId, O.LESSON_LEVEL as LastLevel " +
      "FROM ERC_STUDENT_SCR AS O JOIN " +
      "(SELECT STUDENT_ID, MAX(TARGET_DATE) AS DT FROM ERC_STUDENT_SCR WHERE STUDENT_ID IN ?1 AND COMPLETED = 1 GROUP BY 1) AS T " +
      "WHERE O.STUDENT_ID = T.STUDENT_ID AND O.TARGET_DATE  = T.DT", nativeQuery = true)
  Optional<List<LastLevel>> findLastLevelByStudentIdIn(List<String> studentIds);
}
