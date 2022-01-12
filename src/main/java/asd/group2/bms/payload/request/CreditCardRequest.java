package asd.group2.bms.payload.request;

import javax.validation.constraints.NotNull;

/**
 * @description: Structure of account creation request
 */
public class CreditCardRequest {

  @NotNull(message = "Transaction Limit is required")
  private Integer expectedTransactionLimit;

  public Integer getExpectedTransactionLimit() {
    return expectedTransactionLimit;
  }

  public void setExpectedTransactionLimit(Integer expectedTransactionLimit) {
    this.expectedTransactionLimit = expectedTransactionLimit;
  }

}
