package com.erclub.sms.repositories;

import com.erclub.sms.models.Bill;
import com.erclub.sms.models.BillYearlySummary;
import com.erclub.sms.models.UnpaidBill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface BillRepository extends JpaRepository<Bill, String> {
  Optional<List<Bill>> findByStudentIdInAndTargetMonth(List<String> studentIds, String targetMonth);

  Optional<Bill> findByStudentIdAndTargetMonth(String studentId, String targetMonth);

  @Query("SELECT targetMonth as targetMonth, depositMethod as depositMethod, SUM(tuition) AS totalTuition, SUM(bookPrice) as totalBookPrice, SUM(deposit) as totalDeposit " +
      "FROM Bill " +
      "WHERE studentId IN ?1 AND SUBSTRING(targetMonth, 1, 4) = ?2 " +
      "GROUP BY targetMonth, depositMethod")
  Optional<List<BillYearlySummary>> findYearlySummaryByStudentIdInAndYear(List<String> studentIds, String year);

  @Query("SELECT studentId AS studentId, targetMonth AS targetMonth, tuition + bookPrice - deposit AS unpaid " +
      "FROM Bill " +
      "WHERE studentId IN ?1 AND SUBSTRING(targetMonth, 1, 4) = ?2 AND tuition + bookPrice - deposit > 0")
  Optional<List<UnpaidBill>> findYearlyUnpaidBill(List<String> studentIds, String year);
}
