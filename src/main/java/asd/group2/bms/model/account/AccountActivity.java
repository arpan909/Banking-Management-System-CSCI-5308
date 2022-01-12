package asd.group2.bms.model.account;

import asd.group2.bms.model.audit.DateAudit;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * @description: This will create users table in the database
 */
@Entity
@Table(name = "account_activities")
public class AccountActivity extends DateAudit {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long activityId;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "account_number", nullable = false)
  @OnDelete(action = OnDeleteAction.CASCADE)
  @JsonIgnore
  private Account account;

  @Enumerated(EnumType.STRING)
  @NotNull
  private ActivityType activityType;

  @NotNull
  private Double transactionAmount;

  private String comment;

  public AccountActivity() {
  }

  public AccountActivity(Account account, ActivityType activityType, Double transactionAmount, String comment) {
    this.account = account;
    this.activityType = activityType;
    this.transactionAmount = transactionAmount;
    this.comment = comment;
  }

  public Long getActivityId() {
    return activityId;
  }

  public void setActivityId(Long activityId) {
    this.activityId = activityId;
  }

  public Account getAccount() {
    return account;
  }

  public void setAccount(Account account) {
    this.account = account;
  }

  public ActivityType getActivityType() {
    return activityType;
  }

  public void setActivityType(ActivityType activityType) {
    this.activityType = activityType;
  }

  public Double getTransactionAmount() {
    return transactionAmount;
  }

  public void setTransactionAmount(Double transactionAmount) {
    this.transactionAmount = transactionAmount;
  }

  public String getComment() {
    return comment;
  }

  public void setComment(String comment) {
    this.comment = comment;
  }

}
