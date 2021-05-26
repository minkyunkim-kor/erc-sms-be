package com.erclub.sms.services;

import com.erclub.sms.api.request.SaveScoreRequest;
import com.erclub.sms.common.domain.STUDENT_CATEGORY;
import com.erclub.sms.common.exception.CommonException;
import com.erclub.sms.models.*;
import com.erclub.sms.repositories.LevelTestRepository;
import com.erclub.sms.repositories.ScoreExtraRepository;
import com.erclub.sms.repositories.ScoreRepository;
import com.erclub.sms.services.vo.ReportVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
public class ScoreService {
  private final ScoreRepository scoreRepository;
  private final StudentService studentService;
  private final LessonService lessonService;
  private final LevelTestRepository levelTestRepository;

  public ScoreService(ScoreRepository scoreRepository, StudentService studentService, LessonService lessonService, LevelTestRepository levelTestRepository) {
    this.scoreRepository = scoreRepository;
    this.studentService = studentService;
    this.lessonService = lessonService;
    this.levelTestRepository = levelTestRepository;
  }

  @Transactional(readOnly = true)
  public List<Score> getScores(String userId, String startDate, String endDate) {
    Map<String, String> studentNameMap = studentService.getStudents(userId)
        .stream()
        .collect(Collectors.toMap(Student::getStudentId, Student::getNameKo));
    List<Score> scores = scoreRepository.findByStudentIdInAndTargetDateBetween(new ArrayList<>(studentNameMap.keySet()), startDate, endDate)
        .orElseGet(ArrayList::new)
        .stream()
        .filter(Score::getCompleted)
        .collect(Collectors.toList());
    for (Score score : scores) {
      score.setName(studentNameMap.get(score.getStudentId()));
    }
    return scores;
  }

  @Transactional(readOnly = true)
  public Score getScore(String userId, String studentId, String targetDate) {
    Student student = studentService.getStudent(userId, studentId);
    if (!studentId.equals(student.getStudentId())) {
      throw new CommonException(NOT_FOUND.value(), "not exist student");
    }
    Score score = scoreRepository.findByStudentIdAndTargetDate(studentId, targetDate)
        .orElseThrow(() -> new CommonException(HttpStatus.NOT_FOUND.value(), "not exist score"));
    score.setName(student.getNameKo());
    return score;
  }

  @Transactional(readOnly = true)
  public List<Score> getScoreTargets(String userId, String targetDate) {
    Map<String, Lesson> lessons = lessonService.getLessons(userId, targetDate)
        .stream()
        .collect(Collectors.toMap(Lesson::getLessonId, lesson -> lesson));
    if (lessons.isEmpty()) return new ArrayList<>();
    Map<String, Student> students = studentService.getStudents(userId)
        .stream()
        .filter(
            student -> (lessons.containsValue(student.getLesson())) &&
                STUDENT_CATEGORY.WITHDRAWAL.getValue() != student.getCategory()
        )
        .collect(Collectors.toMap(Student::getStudentId, student -> student));
    if (students.isEmpty()) return new ArrayList<>();
    List<String> studentIds = new ArrayList<>(students.keySet());
    Map<String, String> lastLevels =
        scoreRepository.findLastLevelByStudentIdIn(studentIds).orElseGet(ArrayList::new)
        .stream()
        .collect(Collectors.toMap(LastLevel::getStudentId, LastLevel::getLastLevel));
    Map<String, Score> scores =
        scoreRepository.findByStudentIdInAndTargetDate(studentIds, targetDate).orElseGet(ArrayList::new)
        .stream()
        .collect(Collectors.toMap(Score::getStudentId, score -> score));

    List<Score> result = new ArrayList<>();
    for (Student student : students.values()) {
      if (LocalDate.parse(targetDate).isBefore(LocalDate.parse(student.getRegisteredDate()))) continue;
      Score score = new Score();
      score.setStudentId(student.getStudentId());
      score.setName(student.getNameKo());
      score.setNameEn(student.getNameEn());
      if(scores.containsKey(student.getStudentId())) {
        score.setScoreValue(scores.get(student.getStudentId()));
        if(StringUtils.isBlank(score.getTeacher())) {
          score.setTeacher(lessons.get(student.getLesson().getLessonId()).getTeacherName());
        }
      } else if (lastLevels.containsKey(student.getStudentId())) {
        score.setLessonLevel(lastLevels.get(student.getStudentId()));
      } else {
        Optional<LevelTest> levelTest = levelTestRepository.findById(student.getStudentId());
        score.setLessonLevel(
            levelTest.map(
                test ->
                    Optional.ofNullable(test.getInitLevelA()).orElse("") +
                        Optional.ofNullable(test.getInitLevelB()).orElse("")
            ).orElse("")
        );
      }
      result.add(score);
    }
    return result;
  }

  @Transactional(readOnly = true)
  public List<Score> getScoresForManualInput(String userId) {
    Map<String, Student> students = studentService.getStudents(userId)
        .stream()
        .collect(Collectors.toMap(Student::getStudentId, student -> student));
    if (students.isEmpty()) return new ArrayList<>();
    Map<String, String> lastLevels =
        scoreRepository.findLastLevelByStudentIdIn(new ArrayList<>(students.keySet())).orElseGet(ArrayList::new)
            .stream()
            .collect(Collectors.toMap(LastLevel::getStudentId, LastLevel::getLastLevel));

    List<Score> targets = new ArrayList<>();
    for (Student student : students.values()) {
      Score score = new Score();
      score.setStudentId(student.getStudentId());
      score.setName(student.getNameKo());
      score.setNameEn(student.getNameEn());
      score.setTeacher(student.getLesson().getTeacherName());
      if (lastLevels.containsKey(student.getStudentId())) {
        score.setLessonLevel(lastLevels.get(student.getStudentId()));
      } else {
        Optional<LevelTest> levelTest = levelTestRepository.findById(student.getStudentId());
        score.setLessonLevel(levelTest.map(test -> test.getInitLevelA() + test.getInitLevelB()).orElse(""));
      }
      targets.add(score);
    }
    return targets;
  }

  @Transactional
  public void saveScores(SaveScoreRequest request) {
    List<String> studentIds = request.getInput().stream().map(SaveScoreRequest.ScoreDetail::getStudentId).collect(Collectors.toList());
    Map<String, Score> scores =
        scoreRepository.findByStudentIdInAndTargetDate(studentIds, request.getTargetDate()).orElseGet(ArrayList::new)
            .stream()
            .collect(Collectors.toMap(Score::getStudentId, score -> score));

    List<Score> saveTargets = new ArrayList<>();
    for (SaveScoreRequest.ScoreDetail detail : request.getInput()) {
      Score target = scores.containsKey(detail.getStudentId()) ? scores.get(detail.getStudentId()) : Score.from(detail.getStudentId());
      target.setTargetDate(request.getTargetDate());

      // 태도 평가 점수 입력
      if (null != detail.getAttitude()) {
        target.setLessonLevel(detail.getAttitude().getLessonLevel());
        target.setAbsent(detail.getAttitude().getAbsent());
        target.setScoreA(detail.getAttitude().getAttendance());
        target.setScoreH(detail.getAttitude().getHomework());
        target.setScoreP(detail.getAttitude().getParticipation());
        target.setScoreM(detail.getAttitude().getManner());

        if (detail.getAttitude().getAbsent() && scores.containsKey(detail.getStudentId())) {
          target.setScoreD(null);
          target.setScoreOF(null);
          target.setScoreC(null);
          target.setScoreG(null);
          target.setScoreW(null);
          target.setScoreS(null);
          target.setExtra(null);
        }
      }

      //학습 평가 점수 입력
      if (null != detail.getLearning()) {
        target.setScoreD(detail.getLearning().getDecoding());
        target.setScoreOF(detail.getLearning().getFluency());
        target.setScoreC(detail.getLearning().getComprehension());
        target.setScoreG(detail.getLearning().getGrammar());
        target.setScoreW(detail.getLearning().getWriting());
        target.setScoreS(detail.getLearning().getSpeaking());
      }

      // 주관 평가 점수 입력
      if (null != detail.getExtra()) {
        target.setTeacher(detail.getExtra().getTeacher());
        target.setExtra(detail.getExtra().getComment());
        target.setCompleted(detail.getExtra().getCompleted());
      }
      saveTargets.add(target);
    }
    scoreRepository.saveAll(saveTargets);
  }

  @Transactional
  public void deleteScore(String studentId, String targetDate) {
    Score score = scoreRepository.findByStudentIdAndTargetDate(studentId, targetDate)
        .orElseThrow(() -> new CommonException(HttpStatus.BAD_REQUEST.value(), "Not exist score data"));
    scoreRepository.delete(score);
  }

  @Transactional(readOnly = true)
  public ReportVo getReport(String userId, String studentId, String startDate, String endDate) {
    Student student = studentService.getStudent(userId, studentId);
    ReportVo reportVo = ReportVo.from(student);
    List<Score> scores = scoreRepository.findByStudentIdInAndTargetDateBetween(Collections.singletonList(studentId), startDate, endDate)
        .orElseGet(ArrayList::new)
        .stream()
        .filter(Score::getCompleted)
        .collect(Collectors.toList());
    if (scores.isEmpty()) {
      Optional<LevelTest> levelTest = levelTestRepository.findById(studentId);
      reportVo.setLevel(levelTest.map(test -> test.getInitLevelA() + test.getInitLevelB()).orElse(""));
      return reportVo;
    }
    scores.sort(Comparator.comparing(Score::getTargetDate));

    reportVo.setAverageA(scores.stream().map(Score::getScoreA).filter(Objects::nonNull).mapToInt((a) -> a).summaryStatistics().getAverage());
    reportVo.setAverageH(scores.stream().map(Score::getScoreH).filter(Objects::nonNull).mapToInt((a) -> a).summaryStatistics().getAverage());
    reportVo.setAverageP(scores.stream().map(Score::getScoreP).filter(Objects::nonNull).mapToInt((a) -> a).summaryStatistics().getAverage());
    reportVo.setAverageM(scores.stream().map(Score::getScoreM).filter(Objects::nonNull).mapToInt((a) -> a).summaryStatistics().getAverage());
    reportVo.setAverageD(scores.stream().map(Score::getScoreD).filter(Objects::nonNull).mapToInt((a) -> a).summaryStatistics().getAverage());
    reportVo.setAverageOF(scores.stream().map(Score::getScoreOF).filter(Objects::nonNull).mapToInt((a) -> a).summaryStatistics().getAverage());
    reportVo.setAverageC(scores.stream().map(Score::getScoreC).filter(Objects::nonNull).mapToInt((a) -> a).summaryStatistics().getAverage());
    reportVo.setAverageG(scores.stream().map(Score::getScoreG).filter(Objects::nonNull).mapToInt((a) -> a).summaryStatistics().getAverage());
    reportVo.setAverageW(scores.stream().map(Score::getScoreW).filter(Objects::nonNull).mapToInt((a) -> a).summaryStatistics().getAverage());
    reportVo.setAverageS(scores.stream().map(Score::getScoreS).filter(Objects::nonNull).mapToInt((a) -> a).summaryStatistics().getAverage());
    reportVo.setLevel(scores.get(scores.size() - 1).getLessonLevel());

    return reportVo;
  }
}
