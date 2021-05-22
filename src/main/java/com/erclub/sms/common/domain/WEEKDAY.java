package com.erclub.sms.common.domain;

import org.apache.commons.lang3.StringUtils;

public enum WEEKDAY {
  MONDAY("월", 0),
  TUESDAY("화", 1),
  WEDNESDAY("수", 2),
  THURSDAY("목", 3),
  FRIDAY("금", 4),
  SATURDAY("토", 5),
  SUNDAY("일", 6);

  private String source;
  private int target;

  WEEKDAY(String source, int target) {
    this.source = source;
    this.target = target;
  }

  public String getSource() {
    return source;
  }

  public int getTarget() {
    return target;
  }

  public static WEEKDAY convertBySource(String source) {
    for (WEEKDAY weekday : values()) {
      if (StringUtils.equals(source, weekday.getSource())) {
        return weekday;
      }
    }
    return null;
  }

  public static WEEKDAY convertByTarget(int target) {
    for (WEEKDAY weekday : values()) {
      if (target == weekday.getTarget()) {
        return weekday;
      }
    }
    return null;
  }
}
