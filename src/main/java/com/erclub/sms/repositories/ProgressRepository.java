package com.erclub.sms.repositories;

import com.erclub.sms.models.Progress;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProgressRepository extends JpaRepository<Progress, String> {
  Optional<List<Progress>> findByStudentIdIn(List<String> studentIds);
}
