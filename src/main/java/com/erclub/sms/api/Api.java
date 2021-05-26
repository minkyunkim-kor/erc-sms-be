package com.erclub.sms.api;

import com.erclub.sms.api.dto.*;
import com.erclub.sms.api.request.*;
import com.erclub.sms.common.domain.CommonResponse;
import com.erclub.sms.common.domain.STUDENT_CATEGORY;
import com.erclub.sms.models.Progress;
import com.erclub.sms.models.Score;
import com.erclub.sms.models.Student;
import com.erclub.sms.services.*;
import com.erclub.sms.services.vo.BillSummaryVo;
import com.erclub.sms.services.vo.StudentSummaryVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@Slf4j
public class Api {

  private final UserService userService;
  private final LessonService lessonService;
  private final StudentService studentService;
  private final LevelTestService levelTestService;
  private final ScoreService scoreService;
  private final BillService billService;
  private final CashReceiptService cashReceiptService;
  private final ProgressService progressService;

  public Api(UserService userService, LessonService lessonService, StudentService studentService,
             LevelTestService levelTestService, ScoreService scoreService, BillService billService,
             CashReceiptService cashReceiptService, ProgressService progressService) {
    this.userService = userService;
    this.lessonService = lessonService;
    this.studentService = studentService;
    this.levelTestService = levelTestService;
    this.scoreService = scoreService;
    this.billService = billService;
    this.cashReceiptService = cashReceiptService;
    this.progressService = progressService;
  }

  /**
   * Save User API (for Admin)
   */
  @PostMapping(value = "/user", produces = "application/json; charset=utf8")
  public ResponseEntity<CommonResponse> signUp(
      @RequestHeader(name = "erc-user-id") final String userId,
      @RequestBody final SaveUserRequest request
  ) {
    userService.saveUser(userId, request);

    return ResponseEntity.ok(CommonResponse.of("success"));
  }

  /**
   * Get Users API (for Admin)
   */
  @GetMapping(value = "/user", produces = "application/json; charset=utf8")
  public ResponseEntity<List<UserDto>> getUsers(@RequestHeader(name = "erc-user-id") final String userId) {
    return ResponseEntity.ok(userService.getUsers(userId).stream().map(UserDto::new).collect(Collectors.toList()));
  }

  /**
   * Get User API (for Admin)
   */
  @GetMapping(value = "/user/{userId}", produces = "application/json; charset=utf8")
  public ResponseEntity<UserDto> getUser(
      @RequestHeader(name = "erc-user-id") final String userId,
      @PathVariable(name = "userId") final String targetId
  ) {
    return ResponseEntity.ok(new UserDto(userService.getUser(userId, targetId)));
  }

  /**
   * Delete User API (for Admin)
   */
  @DeleteMapping(value = "/user/{userId}", produces = "application/json; charset=utf8")
  public ResponseEntity<CommonResponse> deleteUser(
      @RequestHeader(name = "erc-user-id") final String userId,
      @PathVariable(name = "userId") final String targetId
  ) {
    userService.deleteUsers(userId, targetId);
    return ResponseEntity.ok(CommonResponse.of("success"));
  }

  /**
   * Sign In API
   */
  @PostMapping(value = "/signin", produces = "application/json; charset=utf8")
  public ResponseEntity<SignInDto> signIn(@RequestBody SignInRequest request) {
    log.info("[SignIn][ID : {}]", request.getId());
    long start = System.currentTimeMillis();
    SignInDto response = new SignInDto(userService.signIn(request.getId(), request.getPw()));
    log.info("[SignIn][ID : {}][Elapsed Time : {}]", request.getId(), calculateElapsedTime(start));
    return ResponseEntity.ok(response);
  }

  /**
   * Get Lessons API
   */
  @GetMapping(value = "/lesson", produces = "application/json; charset=utf8")
  public ResponseEntity<List<LessonDto>> getLessons(@RequestHeader(name = "erc-user-id") final String userId) {
    log.info("[Get Lessons][User Id : {}]", userId);
    long start = System.currentTimeMillis();
    List<LessonDto> response = lessonService.getLessons(userId)
        .stream()
        .map(LessonDto::new)
        .sorted(Comparator.comparing(LessonDto::getName))
        .collect(Collectors.toList());
    log.info("[Get Lessons][User Id : {}][Elapsed Time : {}]", userId, calculateElapsedTime(start));
    return ResponseEntity.ok(response);
  }

  /**
   * Get Lesson API
   */
  @GetMapping(value = "/lesson/{lessonId}", produces = "application/json; charset=utf8")
  public ResponseEntity<LessonDto> getLesson(
      @RequestHeader(name = "erc-user-id") final String userId,
      @PathVariable final String lessonId
  ) {
    log.info("[Get Lesson][User Id : {}][Lesson Id : {}]", userId, lessonId);
    long start = System.currentTimeMillis();
    LessonDto response = new LessonDto(lessonService.getLesson(userId, lessonId));
    log.info("[Get Lesson][User Id : {}][Lesson Id : {}][Elapsed Time : {}]", userId, lessonId, calculateElapsedTime(start));
    return ResponseEntity.ok(response);
  }

  /**
   * Save Lesson API
   */
  @PostMapping(value = "/lesson", produces = "application/json; charset=utf8")
  public ResponseEntity<CommonResponse> saveLesson(
      @RequestHeader(name = "erc-user-id") final String userId,
      @RequestBody final SaveLessonRequest request
  ) {
    request.validate();
    log.info("[Save Lesson][User Id : {}]", userId);
    long start = System.currentTimeMillis();
    lessonService.saveLesson(userId, request);
    log.info("[Save Lesson][User Id : {}][Elapsed Time : {}]", userId, calculateElapsedTime(start));
    return ResponseEntity.ok(CommonResponse.of("success"));
  }

  /**
   * Update Lesson API
   */
  @PostMapping(value = "/lesson/{lessonId}", produces = "application/json; charset=utf8")
  public ResponseEntity<CommonResponse> updateLesson(
      @RequestHeader(name = "erc-user-id") final String userId,
      @PathVariable(name = "lessonId") final String lessonId,
      @RequestBody final SaveLessonRequest request
  ) {
    request.validate();
    log.info("[Update Lesson][User Id : {}][Lesson Id : {}]", userId, lessonId);
    long start = System.currentTimeMillis();
    lessonService.updateLesson(userId, lessonId, request);
    log.info("[Update Lesson][User Id : {}][Lesson Id : {}][Elapsed Time : {}]", userId, lessonId, calculateElapsedTime(start));
    return ResponseEntity.ok(CommonResponse.of("success"));
  }

  /**
   * Delete Lesson API
   */
  @DeleteMapping(value = "/lesson/{lessonId}", produces = "application/json; charset=utf8")
  public ResponseEntity<CommonResponse> deleteLesson(
      @RequestHeader(name = "erc-user-id") final String userId,
      @PathVariable(name = "lessonId") final String lessonId
  ) {
    log.info("[Delete Lesson][User Id : {}][Lesson Id : {}]", userId, lessonId);
    long start = System.currentTimeMillis();
    lessonService.deleteLesson(userId, lessonId);
    log.info("[Delete Lesson][User Id : {}][Lesson Id : {}][Elapsed Time : {}]", userId, lessonId, calculateElapsedTime(start));
    return ResponseEntity.ok(CommonResponse.of("success"));
  }

  /**
   * Get Students API
   */
  @GetMapping(value = "/student", produces = "application/json; charset=utf8")
  public ResponseEntity<List<StudentDto>> getStudents(
      @RequestHeader(name = "erc-user-id") final String userId
  ) {
    log.info("[Get Students][User Id : {}]", userId);
    long start = System.currentTimeMillis();
    List<StudentDto> response = studentService.getStudents(userId)
        .stream()
        .sorted(Comparator.comparing(Student::getCategory, Comparator.nullsLast(Comparator.naturalOrder()))
            .thenComparing(Student::getLesson, Comparator.nullsLast(Comparator.naturalOrder())))
        .map(StudentDto::new)
        .collect(Collectors.toList());
    log.info("[Get Students][User Id : {}][Elapsed Time : {}]", userId, calculateElapsedTime(start));
    return ResponseEntity.ok(response);
  }

  /**
   * Get Student API
   */
  @GetMapping(value = "/student/{studentId}", produces = "application/json; charset=utf8")
  public ResponseEntity<StudentDto> getStudent(
      @RequestHeader(name = "erc-user-id") final String userId,
      @PathVariable(name = "studentId") final String studentId
  ) {
    log.info("[Get Student][User Id : {}][Student Id : {}]", userId, studentId);
    long start = System.currentTimeMillis();
    StudentDto response = new StudentDto(studentService.getStudent(userId, studentId));
    log.info("[Get Student][User Id : {}][Student Id : {}][Elapsed Time : {}]", userId, studentId, calculateElapsedTime(start));
    return ResponseEntity.ok(response);
  }

  /**
   * Save Student API
   */
  @PostMapping(value = "/student", produces = "application/json; charset=utf8")
  public ResponseEntity<CommonResponse> saveStudent(
      @RequestHeader(name = "erc-user-id") final String userId,
      @RequestBody final SaveStudentRequest request
  ) {
    request.validate();
    log.info("[Save Student][User Id : {}]", userId);
    long start = System.currentTimeMillis();
    studentService.saveStudent(userId, request);
    log.info("[Save Student][User Id : {}][Elapsed Time : {}]", userId, calculateElapsedTime(start));
    return ResponseEntity.ok(CommonResponse.of("success"));
  }

  /**
   * Update Student API
   */
  @PostMapping(value = "/student/{studentId}", produces = "application/json; charset=utf8")
  public ResponseEntity<CommonResponse> updateStudentInfo(
      @RequestHeader(name = "erc-user-id") final String userId,
      @PathVariable(name = "studentId") final String studentId,
      @RequestBody final SaveStudentRequest request
  ) {
    request.validate();
    log.info("[Update Student][User Id : {}][Student Id : {}]", userId, studentId);
    long start = System.currentTimeMillis();
    studentService.updateStudent(userId, studentId, request);
    log.info("[Update Student][User Id : {}][Student Id : {}][Elapsed Time : {}]", userId, studentId, calculateElapsedTime(start));
    return ResponseEntity.ok(CommonResponse.of("success"));
  }

  /**
   * Delete Student API
   */
  @DeleteMapping(value = "/student/{studentId}", produces = "application/json; charset=utf8")
  public ResponseEntity<CommonResponse> deleteStudent(
      @RequestHeader(name = "erc-user-id") final String userId,
      @PathVariable(name = "studentId") final String studentId
  ) {
    log.info("[Delete Student][User Id : {}][Student Id : {}]", userId, studentId);
    long start = System.currentTimeMillis();
    studentService.deleteStudent(userId, studentId);
    log.info("[Delete Student][User Id : {}][Student Id : {}][Elapsed Time : {}]", userId, studentId, calculateElapsedTime(start));
    return ResponseEntity.ok(CommonResponse.of("success"));
  }

  /**
   * Get Student Summary API
   */
  @GetMapping(value = "/student/summary", produces = "application/json; charset=utf8")
  public ResponseEntity<StudentSummaryDto> getStudentSummary(
      @RequestHeader(name = "erc-user-id") final String userId
  ) {
    log.info("[Get Student Summary][User Id : {}]", userId);
    long start = System.currentTimeMillis();
    StudentSummaryVo vo = studentService.getStudentSummary(userId);
    log.info("[Get Student Summary][User Id : {}][Elapsed Time : {}]", userId, calculateElapsedTime(start));
    return ResponseEntity.ok(new StudentSummaryDto(vo));
  }

  /**
   * Get Level Tests API
   */
  @GetMapping(value = "/student/test", produces = "application/json; charset=utf8")
  public ResponseEntity<List<LevelTestDto>> findAllStudentLevelTest(
      @RequestHeader(name = "erc-user-id") final String userId
  ) {
    log.info("[Get Level Tests][User Id : {}]", userId);
    long start = System.currentTimeMillis();
    List<LevelTestDto> response = levelTestService.getLevelTests(userId).stream().map(LevelTestDto::new).collect(Collectors.toList());
    log.info("[Get Level Tests][User Id : {}][Elapsed Time : {}]", userId, calculateElapsedTime(start));
    return ResponseEntity.ok(response);
  }

  /**
   * Get Level Test API
   */
  @GetMapping(value = "/student/{studentId}/test", produces = "application/json; charset=utf8")
  public ResponseEntity<LevelTestDto> findStudentLevelTest(
      @RequestHeader(name = "erc-user-id") final String userId,
      @PathVariable(name = "studentId") final String studentId
  ) {
    log.info("[Get Level Test][User Id : {}][Student Id : {}]", userId, studentId);
    long start = System.currentTimeMillis();
    LevelTestDto response = new LevelTestDto(levelTestService.getLevelTest(userId, studentId));
    log.info("[Get Level Tests][User Id : {}][Student Id : {}][Elapsed Time : {}]", userId, studentId, calculateElapsedTime(start));
    return ResponseEntity.ok(response);
  }

  /**
   * Update Level Test API
   */
  @PostMapping(value = "/student/{studentId}/test", produces = "application/json; charset=utf8")
  public ResponseEntity<CommonResponse> updateStudentTestInfo(
      @RequestHeader(name = "erc-user-id") final String userId,
      @PathVariable(name = "studentId") final String studentId,
      @RequestBody final SaveLevelTestRequest request
  ) {
    log.info("[Update Level Test][User Id : {}][Student Id : {}]", userId, studentId);
    long start = System.currentTimeMillis();
    levelTestService.updateLevelTest(userId, studentId, request);
    log.info("[Update Level Tests][User Id : {}][Student Id : {}][Elapsed Time : {}]", userId, studentId, calculateElapsedTime(start));
    return ResponseEntity.ok(CommonResponse.of("success"));
  }

  /**
   * Get Scores API
   */
  @GetMapping(value = "/student/score", produces = "application/json; charset=utf8")
  public ResponseEntity<List<ScoreDto>> getScores(
      @RequestHeader(name = "erc-user-id") final String userId,
      @RequestParam(name = "startDate") final String startDate,
      @RequestParam(name = "endDate") final String endDate
  ) {
    log.info("[Get Scores][User Id : {}][start : {}][end : {}]", userId, startDate, endDate);
    long start = System.currentTimeMillis();
    List<ScoreDto> response = scoreService.getScores(userId, startDate, endDate)
        .stream()
        .sorted(Comparator.comparing(Score::getTargetDate).reversed().thenComparing(Score::getLessonLevel))
        .map(ScoreDto::new)
        .collect(Collectors.toList());
    log.info("[Get Scores][User Id : {}][start : {}][end : {}][Elapsed Time : {}]", userId, startDate, endDate, calculateElapsedTime(start));
    return ResponseEntity.ok(response);
  }

  /**
   * Get Score API
   */
  @GetMapping(value = "/student/{studentId}/score", produces = "application/json; charset=utf8")
  public ResponseEntity<ScoreDto> getStudentScore(
      @RequestHeader(name = "erc-user-id") final String userId,
      @PathVariable final String studentId,
      @RequestParam final String targetDate
  ) {
    log.info("[Get Score][User Id : {}][Student Id : {}][Target Date : {}]", userId, studentId, targetDate);
    long start = System.currentTimeMillis();
    ScoreDto response = new ScoreDto(scoreService.getScore(userId, studentId, targetDate));
    log.info("[Get Score][User Id : {}][Student Id : {}][Target Date : {}][Elapsed Time : {}]", userId, studentId, targetDate, calculateElapsedTime(start));
    return ResponseEntity.ok(response);
  }

  /**
   * Get Score Target API
   */
  @GetMapping(value = "/student/score/target", produces = "application/json; charset=utf8")
  public ResponseEntity<List<ScoreTargetDto>> getScoreTargets(
      @RequestHeader(name = "erc-user-id") final String userId,
      @RequestParam(name = "targetDate") final String targetDate
  ) {
    log.info("[Get Score Targets][User Id : {}][Target Date : {}]", userId, targetDate);
    long start = System.currentTimeMillis();
    List<ScoreTargetDto> response = scoreService.getScoreTargets(userId, targetDate)
        .stream()
        .sorted(Comparator.comparing(Score::getLessonLevel))
        .map(ScoreTargetDto::new)
        .collect(Collectors.toList());
    log.info("[Get Score Targets][User Id : {}][Target Date : {}][Elapsed Time : {}]", userId, targetDate, calculateElapsedTime(start));
    return ResponseEntity.ok(response);
  }

  /**
   * Get Score Target For manual Input API
   */
  @GetMapping(value = "/student/score/manual", produces = "application/json; charset=utf8")
  public ResponseEntity<List<ScoreTargetDto>> getManualInputScoreInfo(
      @RequestHeader(name = "erc-user-id") final String userId
  ) {
    log.info("[Get Score Targets (For Manual Input)][User Id : {}]", userId);
    long start = System.currentTimeMillis();
    List<ScoreTargetDto> response = scoreService.getScoresForManualInput(userId).stream().map(ScoreTargetDto::new).collect(Collectors.toList());
    log.info("[Get Score Targets (For Manual Input)][User Id : {}][Elapsed Time : {}]", userId, calculateElapsedTime(start));
    return ResponseEntity.ok(response);
  }

  /**
   * Save Scores API
   */
  @PostMapping(value = "/student/score", produces = "application/json; charset=utf8")
  public ResponseEntity<CommonResponse> saveScores(
      @RequestHeader(name = "erc-user-id") final String userId,
      @RequestBody final SaveScoreRequest request
  ) {
    log.info("[Save Scores][User Id : {}][Target Date : {}][Target Size : {}]", userId, request.getTargetDate(), request.getInput().size());
    long start = System.currentTimeMillis();
    scoreService.saveScores(request);
    log.info("[Save Scores][User Id : {}][Target Date : {}][Target Size : {}][Elapsed Time : {}]",
        userId, request.getTargetDate(), request.getInput().size(), calculateElapsedTime(start));
    return ResponseEntity.ok(CommonResponse.of("success"));
  }

  /**
   * Delete Scores API
   */
  @DeleteMapping(value = "/student/score", produces = "application/json; charset=utf8")
  public ResponseEntity<CommonResponse> deleteStudentScore(
      @RequestHeader(name = "erc-user-id") final String userId,
      @RequestParam(name = "studentId") final String studentId,
      @RequestParam(name = "targetDate") final String targetDate
  ) {
    log.info("[Delete Score][User Id : {}][Student Id : {}][Target Date : {}]", userId, studentId, targetDate);
    long start = System.currentTimeMillis();
    scoreService.deleteScore(studentId, targetDate);
    log.info("[Delete Score][User Id : {}][Student Id : {}][Target Date : {}][Elapsed Time : {}]", userId, studentId, targetDate, calculateElapsedTime(start));
    return ResponseEntity.ok(CommonResponse.of("success"));
  }

  /**
   * Get Report API
   */
  @GetMapping(value = "/student/score/report", produces = "application/json; charset=utf8")
  public ResponseEntity<ReportDto> getReport(
      @RequestHeader(name = "erc-user-id") final String userId,
      @RequestHeader(name = "student-id") final String studentId,
      @RequestParam(name = "startDate") final String startDate,
      @RequestParam(name = "endDate") final String endDate
  ) {
    log.info("[Get Report][User Id : {}][Student Id : {}][start : {}][end : {}]", userId, studentId, startDate, endDate);
    long start = System.currentTimeMillis();
    ReportDto response = new ReportDto(scoreService.getReport(userId, studentId, startDate, endDate));
    log.info("[Get Report][User Id : {}][Student Id : {}][start : {}][end : {}][Elapsed Time : {}]",
        userId, studentId, startDate, endDate, calculateElapsedTime(start));

    return ResponseEntity.ok(response);
  }

  /**
   * Get Progresses API
   */
  @GetMapping(value = "/student/progress", produces = "application/json; charset=utf8")
  public ResponseEntity<List<ProgressDto>> getProgresses(
      @RequestHeader(name = "erc-user-id") final String userId
  ) {
    log.info("[Get Progresses][User Id : {}]", userId);
    long start = System.currentTimeMillis();
    List<ProgressDto> response = progressService.getProgresses(userId)
        .stream()
        .sorted(Comparator.comparing(Progress::getCategory))
        .map(ProgressDto::new)
        .collect(Collectors.toList());
    log.info("[Get Progresses][User Id : {}][Elapsed Time : {}]", userId, calculateElapsedTime(start));

    return ResponseEntity.ok(response);
  }

  /**
   * Get Progress API
   */
  @GetMapping(value = "/student/{studentId}/progress", produces = "application/json; charset=utf8")
  public ResponseEntity<ProgressDto> getProgress(
      @RequestHeader(name = "erc-user-id") final String userId,
      @PathVariable final String studentId
  ) {
    log.info("[Get Progress][User Id : {}][Student Id : {}]", userId, studentId);
    long start = System.currentTimeMillis();
    ProgressDto response = new ProgressDto(progressService.getProgress(userId, studentId));
    log.info("[Get Progress][User Id : {}][Student Id : {}][Elapsed Time : {}]", userId, studentId, calculateElapsedTime(start));

    return ResponseEntity.ok(response);
  }

  /**
   * Save Progress API
   */
  @PostMapping(value = "/student/progress", produces = "application/json; charset=utf8")
  public ResponseEntity<CommonResponse> saveProgress(
      @RequestHeader(name = "erc-user-id") final String userId,
      @RequestBody final SaveProgressRequest request
  ) {
    log.info("[Save Progress][User Id : {}][Student Id : {}]", userId, request.getStudentId());
    long start = System.currentTimeMillis();
    progressService.saveProgress(request);
    log.info("[Get Progress][User Id : {}][Student Id : {}][Elapsed Time : {}]", userId, request.getStudentId(), calculateElapsedTime(start));

    return ResponseEntity.ok(CommonResponse.of("success"));
  }

  /**
   * Get Bills API
   */
  @GetMapping(value = "/bill", produces = "application/json; charset=utf8")
  public ResponseEntity<List<BillDto>> getBills(
      @RequestHeader(name = "erc-user-id") final String userId,
      @RequestParam(name = "targetMonth") final String targetMonth
  ) {
    log.info("[Get Bills][User Id : {}][Target Month : {}]", userId, targetMonth);
    long start = System.currentTimeMillis();
    List<BillDto> response = billService.getBills(userId, targetMonth).stream().map(BillDto::new).collect(Collectors.toList());
    log.info("[Get Bills][User Id : {}][Target Month : {}][Elapsed Time : {}]", userId, targetMonth, calculateElapsedTime(start));
    return ResponseEntity.ok(response);
  }

  /**
   * Get Bill API
   */
  @GetMapping(value = "/bill/{studentId}", produces = "application/json; charset=utf8")
  public ResponseEntity<BillDto> getBill(
      @RequestHeader(name = "erc-user-id") final String userId,
      @PathVariable(name = "studentId") final String studentId,
      @RequestParam(name = "targetMonth") final String targetMonth
  ) {
    log.info("[Get Bill][User Id : {}][Student Id : {}][Target Month : {}]", userId, studentId, targetMonth);
    long start = System.currentTimeMillis();
    BillDto response = new BillDto(billService.getBill(userId, studentId, targetMonth));
    log.info("[Get Bills][User Id : {}][Student Id : {}][Target Month : {}][Elapsed Time : {}]",
        userId, studentId, targetMonth, calculateElapsedTime(start));
    return ResponseEntity.ok(response);
  }

  /**
   * Save Bill API
   */
  @PostMapping(value = "/bill", produces = "application/json; charset=utf8")
  public ResponseEntity<CommonResponse> saveBill(
      @RequestHeader(name = "erc-user-id") final String userId,
      @RequestBody final SaveBillRequest request
  ) {
    log.info("[Save Bill][User Id : {}][Target Month : {}][Input Size: {}]", userId, request.getTargetMonth(), request.getBillData().size());
    long start = System.currentTimeMillis();
    billService.saveBill(request);
    log.info("[Save Bill][User Id : {}][Target Month : {}][Input Size: {}][Elapsed Time : {}]",
        userId, request.getTargetMonth(), request.getBillData().size(), calculateElapsedTime(start));
    return ResponseEntity.ok(CommonResponse.of("success"));
  }

  /**
   * Bill Payment API(수납 정보 입력)
   */
  @PostMapping(value = "/bill/payment", produces = "application/json; charset=utf8")
  public ResponseEntity<CommonResponse> billPayment(
      @RequestHeader(name = "erc-user-id") final String userId,
      @RequestBody final BillPaymentRequest request
  ) {
    log.info("[Bill Payment][User Id : {}][Student Id: {}][Target Month : {}]", userId, request.getStudentId(), request.getTargetMonth());
    long start = System.currentTimeMillis();
    billService.billPayment(request);
    log.info("[Bill Payment][User Id : {}][Student Id: {}][Target Month : {}][Elapsed Time : {}]",
        userId, request.getStudentId(), request.getTargetMonth(), calculateElapsedTime(start));
    return ResponseEntity.ok(CommonResponse.of("success"));
  }

  /**
   * Bill Summary API(연간 매출 현황 정보)
   */
  @GetMapping(value = "/bill/summary", produces = "application/json; charset=utf8")
  public ResponseEntity<List<BillSummaryDto>> billSummary(
      @RequestHeader(name = "erc-user-id") final String userId,
      @RequestParam(name = "targetYear") final String year) {
    log.info("[Bill Summary][User Id : {}][Target Year : {}]", userId, year);
    long start = System.currentTimeMillis();
    List<BillSummaryDto> response = billService.getBillSummary(userId, year)
        .stream()
        .sorted(Comparator.comparing(BillSummaryVo::getMonth))
        .map(BillSummaryDto::new)
        .collect(Collectors.toList());
    log.info("[Bill Summary][User Id : {}][Target Year : {}][Elapsed Time : {}]", userId, year, calculateElapsedTime(start));
    return ResponseEntity.ok(response);
  }

  /**
   * Unpaid Summary API(연간 미납금 현황 정보)
   */
  @GetMapping(value = "/bill/unpaid", produces = "application/json; charset=utf8")
  public ResponseEntity<List<UnpaidDto>> getUnpaidBills(
      @RequestHeader(name = "erc-user-id") final String userId,
      @RequestParam(name = "targetYear") final String year
  ) {
    log.info("[Unpaid Summary][User Id : {}][Target Year : {}]", userId, year);
    long start = System.currentTimeMillis();
    List<UnpaidDto> response = billService.getUnpaid(userId, year)
        .stream()
        .map(UnpaidDto::new)
        .collect(Collectors.toList());
    log.info("[Unpaid Summary][User Id : {}][Target Year : {}][Elapsed Time : {}]", userId, year, calculateElapsedTime(start));
    return ResponseEntity.ok(response);
  }

  /**
   * Get Cash Receipts API
   */
  @GetMapping(value = "/bill/cashReceipt", produces = "application/json; charset=utf8")
  public ResponseEntity<List<CashReceiptDto>> getCashReceipts(
      @RequestHeader(name = "erc-user-id") final String userId
  ) {
    log.info("[Get Cash Receipts][User Id : {}]", userId);
    long start = System.currentTimeMillis();
    List<CashReceiptDto> response = cashReceiptService.getCashReceipts(userId).stream().map(CashReceiptDto::new).collect(Collectors.toList());
    log.info("[Get Cash Receipts][User Id : {}][Elapsed Time: {}]", userId, calculateElapsedTime(start));
    return ResponseEntity.ok(response);
  }

  /**
   * Get Cash Receipt API
   */
  @GetMapping(value = "/bill/{studentId}/cashReceipt", produces = "application/json; charset=utf8")
  public ResponseEntity<CashReceiptDto> getCashReceipt(
      @RequestHeader(name = "erc-user-id") final String userId,
      @PathVariable final String studentId
  ) {
    log.info("[Get Cash Receipt][User Id : {}][StudentId : {}]", userId, studentId);
    long start = System.currentTimeMillis();
    CashReceiptDto response = new CashReceiptDto(cashReceiptService.getCashReceipt(userId, studentId));
    log.info("[Get Cash Receipt][User Id : {}][StudentId : {}][Elapsed Time: {}]", userId, studentId, calculateElapsedTime(start));
    return ResponseEntity.ok(response);
  }

  /**
   * Save Cash Receipts API
   */
  @PostMapping(value = "/bill/cashReceipt", produces = "application/json; charset=utf8")
  public ResponseEntity<CommonResponse> saveBillCashReceipt(
      @RequestHeader(name = "erc-user-id") final String userId,
      @RequestBody final SaveCashReceiptRequest request
  ) {
    log.info("[Save Cash Receipt][User Id : {}][StudentId : {}]", userId, request.getStudentId());
    long start = System.currentTimeMillis();
    cashReceiptService.saveCashReceipt(request);
    log.info("[Save Cash Receipt][User Id : {}][StudentId : {}][Elapsed Time: {}]", userId, request.getStudentId(), calculateElapsedTime(start));
    return ResponseEntity.ok(CommonResponse.of("success"));
  }

  private long calculateElapsedTime(long start) {
    return System.currentTimeMillis() - start;
  }
}