package com.erclub.sms.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "ERC_STUDENT_BILL")
@Data
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
public class Bill {

  @Id
  @GeneratedValue(generator = "uuid")
  @GenericGenerator(name = "uuid", strategy = "uuid2")
  @Column(name = "STUDENT_BILL_ID")
  private String id;

  @Column(name = "STUDENT_ID")
  private String studentId;

  @Column(name = "TARGET_MONTH")
  private String targetMonth;

  @Column(name = "TUITION")
  private Integer tuition;

  @Column(name = "BOOK_PRICE")
  private Integer bookPrice;

  @Column(name = "DEPOSIT")
  private Integer deposit;

  @Column(name = "DEPOSIT_DATE")
  private String depositDate;

  @Column(name = "DEPOSIT_METHOD")
  private String depositMethod;

  @Column(name = "EXTRA")
  private String extra;

  @Transient
  private String name;

  @Transient
  private String grade;

  @Transient
  private Integer category;

  protected Bill(String studentId, String targetMonth) {
    this.studentId = studentId;
    this.targetMonth = targetMonth;
    this.tuition = 0;
    this.bookPrice = 0;
    this.deposit = 0;
    this.depositDate = "";
    this.depositMethod = "";
    this.extra = "";
  }

  public static Bill from(String studentId, String targetMonth) {
    return new Bill(studentId, targetMonth);
  }
}
