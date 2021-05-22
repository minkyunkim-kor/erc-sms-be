package com.erclub.sms.api.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class SaveScoreRequest {

  private List<ScoreDetail> input;
  private String targetDate;

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  public static class ScoreDetail {
    private String studentId;
    private Attitude attitude;
    private Learning learning;
    private Extra extra;
  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Attitude {
    private String lessonLevel;
    private Boolean absent;
    private Integer attendance;
    private Integer homework;
    private Integer participation;
    private Integer manner;
  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Learning {
    private Integer decoding;
    private Integer fluency;
    private Integer comprehension;
    private Integer grammar;
    private Integer writing;
    private Integer speaking;
  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Extra {
    private String teacher;
    private String comment;
    private Boolean completed;
  }
}
