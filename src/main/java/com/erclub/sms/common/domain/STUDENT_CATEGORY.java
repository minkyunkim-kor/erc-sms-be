package com.erclub.sms.common.domain;

import org.apache.commons.lang3.StringUtils;

import java.util.Comparator;

public enum STUDENT_CATEGORY implements Comparator<STUDENT_CATEGORY> {
  NO_INPUT("", -1),
  NEW("신입생", 0),
  REGISTER("재원생", 1),
  REST("휴원생", 2),
  WITHDRAWAL("퇴원생", 3);

  private final String name;
  private final int value;

  STUDENT_CATEGORY(String name, int value) {
    this.name = name;
    this.value = value;
  }

  public String getName() {
    return name;
  }

  public int getValue() {
    return value;
  }

  public static STUDENT_CATEGORY convertByName(String name) {
    STUDENT_CATEGORY result = null;
    for (STUDENT_CATEGORY category : values()) {
      if (StringUtils.equals(name, category.getName())) {
        result = category;
        break;
      }
    }
    return result;
  }

  public static STUDENT_CATEGORY convertByValue(int value) {
    STUDENT_CATEGORY result = null;
    for (STUDENT_CATEGORY category : values()) {
      if (value == category.getValue()) {
        result = category;
        break;
      }
    }
    return result;
  }

  @Override
  public int compare(STUDENT_CATEGORY o1, STUDENT_CATEGORY o2) {
    return Integer.compare(o1.getValue(), o2.getValue());
  }
}
