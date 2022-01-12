package asd.group2.bms.model.term_deposit;

import asd.group2.bms.model.account.Account;
import asd.group2.bms.model.audit.DateAudit;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @description: This will create term deposit detail table in the database
 */
@Entity
@Table(name = "term_deposit_details")
public class TermDepositDetail extends DateAudit {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long termDepositId;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "account_number", nullable = false)
  @OnDelete(action = OnDeleteAction.CASCADE)
  @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
  private Account account;

  @NotNull
  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private Date startDate;

  @NotNull
  private Double initialAmount;

  @NotNull
  private int duration;

  @NotNull
  private float rateOfInterest;

  @NotNull
  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private Date maturityDate;

  @NotNull
  private Double maturityAmount;

  @Enumerated(EnumType.STRING)
  @NotNull
  private TermDepositStatus termDepositStatus;

  public TermDepositDetail() {
  }

  public TermDepositDetail(Account account, Date startDate,
                           Double initialAmount, int duration, float rateOfInterest,
                           Date maturityDate, Double maturityAmount, TermDepositStatus termDepositStatus) {
    this.account = account;
    this.startDate = startDate;
    this.initialAmount = initialAmount;
    this.duration = duration;
    this.rateOfInterest = rateOfInterest;
    this.maturityDate = maturityDate;
    this.maturityAmount = maturityAmount;
    this.termDepositStatus = termDepositStatus;
  }

  public Long getTermDepositId() {
    return termDepositId;
  }

  public void setTermDepositId(Long termDepositId) {
    this.termDepositId = termDepositId;
  }

  public Account getAccount() {
    return account;
  }

  public void setAccount(Account account) {
    this.account = account;
  }

  public Date getStartDate() {
    return startDate;
  }

  public void setStartDate(Date startDate) {
    this.startDate = startDate;
  }

  public Double getInitialAmount() {
    return initialAmount;
  }

  public void setInitialAmount(Double initialAmount) {
    this.initialAmount = initialAmount;
  }

  public int getDuration() {
    return duration;
  }

  public void setDuration(int duration) {
    this.duration = duration;
  }

  public float getRateOfInterest() {
    return rateOfInterest;
  }

  public void setRateOfInterest(float rateOfInterest) {
    this.rateOfInterest = rateOfInterest;
  }

  public Date getMaturityDate() {
    return maturityDate;
  }

  public void setMaturityDate(Date maturityDate) {
    this.maturityDate = maturityDate;
  }

  public Double getMaturityAmount() {
    return maturityAmount;
  }

  public void setMaturityAmount(Double maturityAmount) {
    this.maturityAmount = maturityAmount;
  }

  public TermDepositStatus getTermDepositStatus() {
    return termDepositStatus;
  }

  public void setTermDepositStatus(TermDepositStatus termDepositStatus) {
    this.termDepositStatus = termDepositStatus;
  }

}
