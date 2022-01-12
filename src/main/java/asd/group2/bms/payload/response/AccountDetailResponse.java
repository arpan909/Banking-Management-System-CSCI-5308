package asd.group2.bms.payload.response;

import asd.group2.bms.model.account.AccountType;

import java.time.Instant;

/**
 * @description: This class will be responsible to return account details.
 */
public class AccountDetailResponse {

  private Long accountNumber;

  private AccountType accountType;

  private Double balance;

  private int creditScore;

  private Instant accountCreatedAt;

  private Instant lastActivityAt;

  private UserMetaResponse userMetaResponse;

  private Long debitCardNumber;

  public AccountDetailResponse() {
  }

  public AccountDetailResponse(Long accountNumber, AccountType accountType, Double balance, int creditScore, Instant accountCreatedAt, Instant lastActivityAt, UserMetaResponse userMetaResponse) {
    this.accountNumber = accountNumber;
    this.accountType = accountType;
    this.balance = balance;
    this.creditScore = creditScore;
    this.accountCreatedAt = accountCreatedAt;
    this.lastActivityAt = lastActivityAt;
    this.userMetaResponse = userMetaResponse;
  }

  public AccountDetailResponse(Long accountNumber, AccountType accountType, Double balance, int creditScore, Instant accountCreatedAt, Instant lastActivityAt, UserMetaResponse userMetaResponse, Long debitCardNumber) {
    this.accountNumber = accountNumber;
    this.accountType = accountType;
    this.balance = balance;
    this.creditScore = creditScore;
    this.accountCreatedAt = accountCreatedAt;
    this.lastActivityAt = lastActivityAt;
    this.userMetaResponse = userMetaResponse;
    this.debitCardNumber = debitCardNumber;
  }

  public Long getAccountNumber() {
    return accountNumber;
  }

  public void setAccountNumber(Long accountNumber) {
    this.accountNumber = accountNumber;
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

  public Instant getAccountCreatedAt() {
    return accountCreatedAt;
  }

  public void setAccountCreatedAt(Instant accountCreatedAt) {
    this.accountCreatedAt = accountCreatedAt;
  }

  public Instant getLastActivityAt() {
    return lastActivityAt;
  }

  public void setLastActivityAt(Instant lastActivityAt) {
    this.lastActivityAt = lastActivityAt;
  }

  public UserMetaResponse getUserMetaResponse() {
    return userMetaResponse;
  }

  public void setUserMetaResponse(UserMetaResponse userMetaResponse) {
    this.userMetaResponse = userMetaResponse;
  }

  public Long getDebitCardNumber() {
    return debitCardNumber;
  }

  public void setDebitCardNumber(Long debitCardNumber) {
    this.debitCardNumber = debitCardNumber;
  }

}
