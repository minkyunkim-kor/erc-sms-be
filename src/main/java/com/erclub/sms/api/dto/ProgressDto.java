package com.erclub.sms.api.dto;

import com.erclub.sms.models.Progress;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class ProgressDto {
  private String studentId;
  private String name;
  private String nameEn;
  private String category;
  private String currentLevel;
  private List<String> progresses;

  public ProgressDto(Progress progress) {
    studentId = progress.getStudentId();
    name = progress.getName();
    nameEn = progress.getNameEn();
    category = progress.getCategory().getName();
    currentLevel = progress.getCurrentLevel();
    progresses = progress.getProgresses();
  }
}
