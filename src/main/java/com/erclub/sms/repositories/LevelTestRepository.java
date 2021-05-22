package com.erclub.sms.repositories;

import com.erclub.sms.models.LevelTest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LevelTestRepository extends JpaRepository<LevelTest, String> {
  Optional<List<LevelTest>> findByStudentIdIn(List<String> studentIds);
}
