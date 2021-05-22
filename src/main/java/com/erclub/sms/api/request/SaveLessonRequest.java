package com.erclub.sms.api.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SaveLessonRequest {
  private String name;
  private String teacherName;
  private List<Time> times;

  public void validate() {
    checkArgument(isNotBlank(name), "lesson name");
    checkArgument(isNotBlank(teacherName), "teacher name");
    checkArgument(null != times, "lesson time info");
    checkArgument(times.size() >= 1, "minimum time info is 1");
    for (SaveLessonRequest.Time time : times) {
      checkArgument(isNotBlank(time.getWeekday()), "weekday of lesson time");
      checkArgument(isNotBlank(time.getStart()), "start of lesson time");
      checkArgument(isNotBlank(time.getEnd()), "end of lesson time");
    }
  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Time {
    private String weekday;
    private String start;
    private String end;
  }
}
