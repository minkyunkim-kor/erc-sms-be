package com.erclub.sms.repositories;

import com.erclub.sms.models.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, String> {
  Optional<List<Student>> findByUserId(String userId);

  Optional<Student> findByUserIdAndStudentId(String userId, String studentId);
}
