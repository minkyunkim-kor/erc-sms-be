package com.erclub.sms.models;

public interface BillYearlySummary {
  String getTargetMonth();
  String getDepositMethod();
  Integer getTotalTuition();
  Integer getTotalBookPrice();
  Integer getTotalDeposit();
}
