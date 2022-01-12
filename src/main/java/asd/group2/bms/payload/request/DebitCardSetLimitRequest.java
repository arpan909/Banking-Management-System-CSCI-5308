package asd.group2.bms.payload.request;

import javax.validation.constraints.NotNull;

public class DebitCardSetLimitRequest {

  @NotNull(message = "Debit card number is required to set the limit")
  private Long debitCardNumber;

  @NotNull(message = "Required transaction limit to update the limit")
  private Integer transactionLimit;

  public Long getDebitCardNumber() {
    return debitCardNumber;
  }

  public void setDebitCardNumber(Long debitCardNumber) {
    this.debitCardNumber = debitCardNumber;
  }

  public Integer getTransactionLimit() {
    return transactionLimit;
  }

  public void setTransactionLimit(Integer transactionLimit) {
    this.transactionLimit = transactionLimit;
  }

}
