package asd.group2.bms.payload.request;

import javax.validation.constraints.NotNull;

/**
 * @description: Structure of Deposit and Withdrawal request
 */
public class DepositWithdrawalRequest {

  @NotNull
  private Long accountNumber;

  @NotNull
  private Double balance;

  public Long getAccountNumber() {

    return accountNumber;
  }

  public void setAccountNumber(Long accountNumber) {

    this.accountNumber = accountNumber;
  }

  public Double getBalance() {

    return balance;
  }

  public void setBalance(Double balance) {

    this.balance = balance;
  }

}
