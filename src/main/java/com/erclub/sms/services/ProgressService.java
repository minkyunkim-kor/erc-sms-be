package com.erclub.sms.services;

import com.erclub.sms.api.request.SaveProgressRequest;
import com.erclub.sms.models.Progress;
import com.erclub.sms.models.Student;
import com.erclub.sms.repositories.ProgressRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProgressService {
  private final ProgressRepository progressRepository;
  private final StudentService studentService;

  private final static List<String> levels = Arrays.asList("A", "B", "C", "P", "D", "E", "F", "G", "H", "I", "J", "K");

  public ProgressService(ProgressRepository progressRepository, StudentService studentService) {
    this.progressRepository = progressRepository;
    this.studentService = studentService;
  }

  @Transactional(readOnly = true)
  public List<Progress> getProgresses(String userId) {
    Map<String, Student> students = studentService.getStudents(userId)
        .stream()
        .collect(Collectors.toMap(Student::getStudentId, student -> student));
    Map<String, Progress> progresses =
        progressRepository.findByStudentIdIn(new ArrayList<>(students.keySet())).orElseGet(ArrayList::new)
        .stream()
        .collect(Collectors.toMap(Progress::getStudentId, progress -> progress));
    for (String studentId : students.keySet()) {
      Progress progress = progresses.getOrDefault(studentId, Progress.from(studentId));
      progress.setStudent(students.get(studentId));
      progress.convertProgress();
      progresses.put(studentId, progress);
    }
    return new ArrayList<>(progresses.values());
  }

  @Transactional(readOnly = true)
  public Progress getProgress(String userId, String studentId) {
    Student student = studentService.getStudent(userId, studentId);
    Progress progress = progressRepository.findById(studentId).orElseGet(() -> Progress.from(studentId));
    progress.setStudent(student);
    progress.convertProgress();
    return progress;
  }

  @Transactional
  public void saveProgress(SaveProgressRequest request) {
    Progress progress = Progress.from(request.getStudentId());
    if (request.getProgresses().size() > 0) {
      request.getProgresses().sort((o1, o2) -> {
        int levelIdx1 = levels.indexOf(o1.substring(0, 1));
        int levelIdx2 = levels.indexOf(o2.substring(0, 1));
        return Integer.compare(levelIdx1, levelIdx2);
      });
      progress.setCurrentLevel(request.getProgresses().get(request.getProgresses().size() - 1));
      progress.setProgress(StringUtils.join(request.getProgresses(), ","));
    } else {
      progress.setCurrentLevel("-");
      progress.setProgresses(Collections.emptyList());
    }
    progressRepository.save(progress);
  }
}
