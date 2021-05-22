package com.erclub.sms.services;

import com.erclub.sms.api.request.SaveStudentRequest;
import com.erclub.sms.common.domain.STUDENT_CATEGORY;
import com.erclub.sms.common.exception.CommonException;
import com.erclub.sms.models.Lesson;
import com.erclub.sms.models.Student;
import com.erclub.sms.repositories.LessonRepository;
import com.erclub.sms.repositories.StudentRepository;
import com.erclub.sms.services.vo.StudentSummaryVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
public class StudentService {

  private final StudentRepository studentRepository;
  private final LessonRepository lessonRepository;

  private static final String YEAR_MONTH_FORMAT = "yyyy-MM";

  public StudentService(StudentRepository studentRepository, LessonRepository lessonRepository) {
    this.studentRepository = studentRepository;
    this.lessonRepository = lessonRepository;
  }

  @Transactional(readOnly = true)
  public List<Student> getStudents(String userId) {
    return studentRepository.findByUserId(userId).orElseGet(ArrayList::new);
  }

  @Transactional(readOnly = true)
  public Student getStudent(String userId, String studentId) {
    Optional<Student> student = studentRepository.findByUserIdAndStudentId(userId, studentId);
    if (student.isPresent()) return student.get();
    else throw new CommonException(NOT_FOUND.value(), "not exist student");
  }

  @Transactional
  public void saveStudent(String userId, SaveStudentRequest request) {
    Lesson lesson = getLesson(userId, request.getLessonName());
    Student student = Student.from(userId,
        STUDENT_CATEGORY.convertByName(request.getCategory()).getValue(),
        request.getRegisteredDate(),
        request.getNameKorean(),
        request.getNameEnglish(),
        request.getGrade(),
        request.getGender(),
        request.getSchool(),
        request.getContact(),
        request.getAddress(),
        request.getExtra(),
        lesson);
    studentRepository.save(student);
  }

  @Transactional
  public void updateStudent(String userId, String studentId, SaveStudentRequest request) {
    Optional<Student> optStudent = studentRepository.findByUserIdAndStudentId(userId, studentId);
    if (!optStudent.isPresent()) throw new CommonException(HttpStatus.NOT_FOUND.value(), "not exist student");

    Lesson lesson = getLesson(userId, request.getLessonName());
    Student student = optStudent.get();
    student.setCategory(STUDENT_CATEGORY.convertByName(request.getCategory()).getValue());
    student.setRegisteredDate(request.getRegisteredDate());
    student.setNameKo(request.getNameKorean());
    student.setNameEn(request.getNameEnglish());
    student.setGrade(request.getGrade());
    student.setGender(request.getGender());
    student.setSchool(request.getSchool());
    student.setContact(request.getContact());
    student.setAddr(request.getAddress());
    student.setExtra(request.getExtra());
    student.setLesson(lesson);

    studentRepository.save(student);
  }

  @Transactional
  public void deleteStudent(String userId, String studentId) {
    Optional<Student> optStudent = studentRepository.findByUserIdAndStudentId(userId, studentId);
    if (!optStudent.isPresent()) throw new CommonException(HttpStatus.NOT_FOUND.value(), "not exist student");
    studentRepository.delete(optStudent.get());
  }

  @Transactional(readOnly = true)
  public StudentSummaryVo getStudentSummary(String userId) {
    Map<String, Integer> register = new ConcurrentHashMap<>();
    LocalDate current = LocalDate.now();
    for (LocalDate index = current.minusMonths(11); Period.between(index, current).getMonths() >= 0; index = index.plusMonths(1)) {
      register.put(index.format(DateTimeFormatter.ofPattern(YEAR_MONTH_FORMAT)), 0);
    }
    StudentSummaryVo.CategorySummary categorySummary = StudentSummaryVo.CategorySummary.of(0, 0, 0, 0);

    List<Student> students = getStudents(userId);
    if(students.isEmpty()) {
      return StudentSummaryVo.of(0, categorySummary, register);
    }

    for (Student student: students) {
      switch (STUDENT_CATEGORY.convertByValue(student.getCategory())) {
        case NEW:
          categorySummary.setNewly(categorySummary.getNewly() + 1);
          break;
        case REGISTER:
          categorySummary.setRegistered(categorySummary.getRegistered() + 1);
          break;
        case REST:
          categorySummary.setRest(categorySummary.getRest() + 1);
          break;
        case WITHDRAWAL:
          categorySummary.setWithdrawal(categorySummary.getWithdrawal() + 1);
          break;
      }
      LocalDate registeredDate = LocalDate.parse(student.getRegisteredDate());
      if(Period.between(registeredDate, current).toTotalMonths() < 11) {
        String month = registeredDate.format(DateTimeFormatter.ofPattern(YEAR_MONTH_FORMAT));
        register.put(month, register.getOrDefault(month, 0) + 1);
      }
    }

    return StudentSummaryVo.of(students.size(), categorySummary, register);
  }

  private Lesson getLesson(String userId, String lessonName) {
    return StringUtils.isNotEmpty(lessonName)
        ? lessonRepository.findByUserIdAndLessonName(userId, lessonName).orElseGet(null)
        : null;
  }
}
