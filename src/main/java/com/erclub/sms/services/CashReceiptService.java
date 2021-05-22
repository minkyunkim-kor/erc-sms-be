package com.erclub.sms.services;

import com.erclub.sms.api.request.SaveCashReceiptRequest;
import com.erclub.sms.common.exception.CommonException;
import com.erclub.sms.models.CashReceipt;
import com.erclub.sms.models.Student;
import com.erclub.sms.repositories.CashReceiptRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
public class CashReceiptService {
  private final CashReceiptRepository cashReceiptRepository;
  private final StudentService studentService;

  public CashReceiptService(CashReceiptRepository cashReceiptRepository, StudentService studentService) {
    this.cashReceiptRepository = cashReceiptRepository;
    this.studentService = studentService;
  }

  @Transactional(readOnly = true)
  public List<CashReceipt> getCashReceipts(String userId) {
    Map<String, Student> students = studentService.getStudents(userId).stream().collect(Collectors.toMap(Student::getStudentId, student -> student));
    Map<String, CashReceipt> cashReceipts = cashReceiptRepository
        .findByStudentIdIn(new ArrayList<>(students.keySet()))
        .orElseGet(ArrayList::new)
        .stream()
        .collect(Collectors.toMap(CashReceipt::getStudentId, cashReceipt -> cashReceipt));
    for(String studentId : students.keySet()) {
      CashReceipt cashReceipt = cashReceipts.getOrDefault(studentId, CashReceipt.from(studentId));
      cashReceipt.setName(students.get(cashReceipt.getStudentId()).getNameKo());
      cashReceipt.setGrade(students.get(cashReceipt.getStudentId()).getGrade());
      cashReceipts.put(studentId, cashReceipt);
    }
    return new ArrayList<>(cashReceipts.values());
  }

  @Transactional(readOnly = true)
  public CashReceipt getCashReceipt(String userId, String studentId) {
    Student student = studentService.getStudent(userId, studentId);
    if (!studentId.equals(student.getStudentId())) {
      throw new CommonException(NOT_FOUND.value(), "not exist student");
    }
    CashReceipt cashReceipt = cashReceiptRepository.findById(studentId).orElseGet(() -> CashReceipt.from(studentId));
    cashReceipt.setName(student.getNameKo());
    cashReceipt.setGrade(student.getGrade());
    return cashReceipt;
  }

  @Transactional
  public void saveCashReceipt(SaveCashReceiptRequest request) {
    CashReceipt cashReceipt = cashReceiptRepository.findById(request.getStudentId()).orElseGet(() -> CashReceipt.from(request.getStudentId()));
    cashReceipt.setCashReceiptInfo(request.getCashReceipt());
    cashReceipt.setExtra(request.getComment());
    cashReceiptRepository.save(cashReceipt);
  }
}
