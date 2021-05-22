package com.erclub.sms.services;

import com.erclub.sms.api.request.BillPaymentRequest;
import com.erclub.sms.api.request.SaveBillRequest;
import com.erclub.sms.common.domain.STUDENT_CATEGORY;
import com.erclub.sms.common.exception.CommonException;
import com.erclub.sms.models.Bill;
import com.erclub.sms.models.BillYearlySummary;
import com.erclub.sms.models.Student;
import com.erclub.sms.models.UnpaidBill;
import com.erclub.sms.repositories.BillRepository;
import com.erclub.sms.services.vo.BillSummaryVo;
import com.erclub.sms.services.vo.UnpaidVo;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class BillService {
  private final BillRepository billRepository;
  private final StudentService studentService;

  public BillService(BillRepository billRepository, StudentService studentService) {
    this.billRepository = billRepository;
    this.studentService = studentService;
  }

  @Transactional(readOnly = true)
  public List<Bill> getBills(String userId, String targetMonth) {
    Map<String, Student> students = studentService.getStudents(userId)
        .stream()
        .filter(student -> !LocalDate.parse(student.getRegisteredDate()).isAfter(LocalDate.parse(targetMonth + "-01")))
        .collect(Collectors.toMap(Student::getStudentId, student -> student));

    Map<String, Bill> billMap =
        billRepository.findByStudentIdInAndTargetMonth(new ArrayList<>(students.keySet()), targetMonth)
            .orElseGet(ArrayList::new)
            .stream()
            .collect(Collectors.toMap(Bill::getStudentId, bill -> bill));

    for (String studentId : students.keySet()) {
      if (!billMap.containsKey(studentId)) {
        billMap.put(studentId, Bill.from(studentId, targetMonth));
      }
      billMap.get(studentId).setName(students.get(studentId).getNameKo());
      billMap.get(studentId).setGrade(students.get(studentId).getGrade());
      billMap.get(studentId).setCategory(students.get(studentId).getCategory());
    }

    return new ArrayList<>(billMap.values());
  }

  @Transactional(readOnly = true)
  public Bill getBill(String userId, String studentId, String targetMonth) {
    Bill bill = billRepository
        .findByStudentIdAndTargetMonth(studentId, targetMonth)
        .orElseGet(() -> Bill.from(studentId, targetMonth));
    Student student = studentService.getStudent(userId, studentId);
    bill.setName(student.getNameKo());
    bill.setGrade(student.getGrade());

    return bill;
  }

  @Transactional
  public void saveBill(SaveBillRequest request) {
    Map<String, Bill> bills =
        billRepository.findByStudentIdInAndTargetMonth(
            request.getBillData()
                .stream()
                .map(SaveBillRequest.BillData::getStudentId)
                .collect(Collectors.toList()),
            request.getTargetMonth()
        ).orElseGet(Collections::emptyList)
        .stream()
        .collect(Collectors.toMap(Bill::getStudentId, bill -> bill));
    for (SaveBillRequest.BillData billData : request.getBillData()) {
      Bill bill = bills.getOrDefault(billData.getStudentId(), Bill.from(billData.getStudentId(), request.getTargetMonth()));
      bill.setTuition(billData.getTuition());
      bill.setBookPrice(billData.getBookPrice());
      bill.setExtra(billData.getComment());
      bills.put(bill.getStudentId(), bill);
    }
    billRepository.saveAll(new ArrayList<>(bills.values()));
  }

  @Transactional
  public void billPayment(BillPaymentRequest request) {
    Bill bill = billRepository.findByStudentIdAndTargetMonth(request.getStudentId(), request.getTargetMonth())
        .orElseThrow(() -> new CommonException(HttpStatus.BAD_REQUEST.value(), "Not Exist Bill Info"));
    bill.setDeposit(request.getDeposit());
    bill.setDepositDate(request.getDepositDate());
    bill.setDepositMethod(request.getDepositMethod());
    bill.setExtra(request.getComment());
    billRepository.save(bill);
  }

  @Transactional(readOnly = true)
  public List<BillSummaryVo> getBillSummary(String userId, String year) {
    List<String> studentIds = studentService.getStudents(userId).stream().map(Student::getStudentId).collect(Collectors.toList());
    List<BillYearlySummary> summaries = billRepository.findYearlySummaryByStudentIdInAndYear(studentIds, year).orElseGet(ArrayList::new);

    List<BillSummaryVo> billSummaryVos = new ArrayList<>();

    for (LocalDate date = LocalDate.of(Integer.parseInt(year), 1, 1);
         !date.equals(LocalDate.of(Integer.parseInt(year) + 1, 1, 1));
         date= date.plusMonths(1)) {
      String month = date.format(DateTimeFormatter.ofPattern("yyyy-MM"));
      BillSummaryVo vo = BillSummaryVo.from(date.getMonthValue());
      List<BillYearlySummary> monthlySummary = summaries.stream()
          .filter(billYearlySummary -> billYearlySummary.getTargetMonth().equals(month))
          .collect(Collectors.toList());
      for (BillYearlySummary summary : monthlySummary) {
        vo.setTuition(vo.getTuition() + summary.getTotalTuition());
        vo.setBookPrice(vo.getBookPrice() + summary.getTotalBookPrice());
        vo.setDeposit(vo.getDeposit() + summary.getTotalDeposit());
        if (summary.getDepositMethod().equals("CARD")) {
          vo.setCard(vo.getCard() + summary.getTotalDeposit());
        } else if (summary.getDepositMethod().equals("CASH")) {
          vo.setCard(vo.getCash() + summary.getTotalDeposit());
        }
      }
      billSummaryVos.add(vo);
    }
    return billSummaryVos;
  }

  @Transactional(readOnly = true)
  public List<UnpaidVo> getUnpaid(String userId, String year) {
    Map<String, Student> students = studentService.getStudents(userId).stream().collect(Collectors.toMap(Student::getStudentId, student -> student));
    List<UnpaidBill> unpaidBills = billRepository.findYearlyUnpaidBill(new ArrayList<>(students.keySet()), year).orElseGet(ArrayList::new);
    Map<String, UnpaidVo> result = new HashMap<>();
    for (UnpaidBill unpaidBill : unpaidBills) {
      UnpaidVo vo = result.getOrDefault(
          unpaidBill.getStudentId(),
          UnpaidVo.from(
              students.get(unpaidBill.getStudentId()).getNameKo(),
              students.get(unpaidBill.getStudentId()).getGrade()
          )
      );
      vo.setUnpaid(LocalDate.parse(unpaidBill.getTargetMonth() + "-01").getMonthValue(), unpaidBill.getUnpaid());
      result.put(unpaidBill.getStudentId(), vo);
    }
    return new ArrayList<>(result.values());
  }
}
