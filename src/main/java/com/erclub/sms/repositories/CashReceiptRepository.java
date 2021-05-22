package com.erclub.sms.repositories;

import com.erclub.sms.models.CashReceipt;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CashReceiptRepository extends JpaRepository<CashReceipt, String> {
  Optional<List<CashReceipt>> findByStudentIdIn(List<String> studentIds);
}
