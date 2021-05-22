package com.erclub.sms.services;

import com.erclub.sms.api.request.SaveLevelTestRequest;
import com.erclub.sms.common.exception.CommonException;
import com.erclub.sms.models.LevelTest;
import com.erclub.sms.models.Student;
import com.erclub.sms.repositories.LevelTestRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
public class LevelTestService {
  private final LevelTestRepository levelTestRepository;
  private final StudentService studentService;

  public LevelTestService(LevelTestRepository levelTestRepository, StudentService studentService) {
    this.levelTestRepository = levelTestRepository;
    this.studentService = studentService;
  }

  public List<LevelTest> getLevelTests(String userId) {
    Map<String, Student> studentMap = studentService.getStudents(userId).stream().collect(Collectors.toMap(Student::getStudentId, student -> student));
    if (studentMap.isEmpty()) return new ArrayList<>();
    Map<String, LevelTest> levelTestMap = levelTestRepository.findByStudentIdIn(new ArrayList<>(studentMap.keySet()))
        .orElseGet(ArrayList::new)
        .stream()
        .collect(Collectors.toMap(LevelTest::getStudentId, levelTest -> levelTest));
    List<LevelTest> levelTests = new ArrayList<>();
    for (String studentId : studentMap.keySet()) {
      Student student = studentMap.get(studentId);
      LevelTest levelTest = levelTestMap.getOrDefault(studentId, LevelTest.from(studentId));
      levelTest.addStudentInfo(student);
      levelTests.add(levelTest);
    }
    return levelTests;
  }

  public LevelTest getLevelTest(String userId, String studentId) {
    Student student = studentService.getStudent(userId, studentId);
    if (!studentId.equals(student.getStudentId())) {
      throw new CommonException(NOT_FOUND.value(), "not exist student");
    }
    LevelTest levelTest = levelTestRepository.findById(studentId).orElseGet(() -> LevelTest.from(studentId));
    levelTest.addStudentInfo(student);
    return levelTest;
  }

  public void updateLevelTest(String userId, String studentId, SaveLevelTestRequest request) {
    if (!studentId.equals(studentService.getStudent(userId, studentId).getStudentId())) {
      throw new CommonException(NOT_FOUND.value(), "not exist student");
    }
    levelTestRepository.save(LevelTest.from(studentId, request));
  }
}
