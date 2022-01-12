package asd.group2.bms.payload.request;

import javax.validation.constraints.NotNull;

public class StartChequeRequest {

  @NotNull
  private long accountNumberSender;

  @NotNull
  private long accountNumberReceiver;

  @NotNull
  private long chequeNumber;

  @NotNull
  private Double amount;

  public long getAccountNumberSender() {
    return accountNumberSender;
  }

  public void setAccountNumberSender(long accountNumberSender) {
    this.accountNumberSender = accountNumberSender;
  }

  public long getAccountNumberReceiver() {
    return accountNumberReceiver;
  }

  public void setAccountNumberReceiver(long accountNumberReceiver) {
    this.accountNumberReceiver = accountNumberReceiver;
  }

  public long getChequeNumber() {
    return chequeNumber;
  }

  public void setChequeNumber(long chequeNumber) {
    this.chequeNumber = chequeNumber;
  }

  public Double getAmount() {
    return amount;
  }

  public void setAmount(Double amount) {
    this.amount = amount;
  }

}
