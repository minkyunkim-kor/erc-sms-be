package com.erclub.sms.models;

public interface UnpaidBill {
  String getStudentId();
  String getTargetMonth();
  Integer getUnpaid();
}
