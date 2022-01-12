package asd.group2.bms.model.account;

import asd.group2.bms.model.audit.DateAudit;
import asd.group2.bms.model.user.User;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @description: This will create accounts table in the database
 */
@Entity
@Table(name = "accounts")
public class Account extends DateAudit implements Serializable {

  @Id
  private Long accountNumber;

  @OneToOne
  @JoinColumn(name = "user_id", nullable = false)
  @OnDelete(action = OnDeleteAction.CASCADE)
  private User user;

  @Enumerated(EnumType.STRING)
  @NotNull
  private AccountType accountType;

  @NotNull
  private Double balance;

  @NotNull
  private int creditScore;

  public Account() {
  }

  public Account(Long accountNumber, AccountType accountType, Double balance,
                 int creditScore) {
    this.accountNumber = accountNumber;
    this.accountType = accountType;
    this.balance = balance;
    this.creditScore = creditScore;
  }

  public Long getAccountNumber() {
    return accountNumber;
  }

  public void setAccountNumber(Long accountNumber) {
    this.accountNumber = accountNumber;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public AccountType getAccountType() {
    return accountType;
  }

  public void setAccountType(AccountType accountType) {
    this.accountType = accountType;
  }

  public Double getBalance() {
    return balance;
  }

  public void setBalance(Double balance) {
    this.balance = balance;
  }

  public int getCreditScore() {
    return creditScore;
  }

  public void setCreditScore(int creditScore) {
    this.creditScore = creditScore;
  }

}

