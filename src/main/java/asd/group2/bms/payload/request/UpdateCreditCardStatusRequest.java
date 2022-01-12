package asd.group2.bms.payload.request;

import asd.group2.bms.model.cards.credit.CreditCardStatus;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;

/**
 * @description: Structure of credit card body request
 */
public class UpdateCreditCardStatusRequest {

  @NotNull(message = "Credit Card Number is required")
  private Long creditCardNumber;

  @Enumerated(EnumType.STRING)
  private CreditCardStatus creditCardStatus;

  public Long getCreditCardNumber() {
    return creditCardNumber;
  }

  public void setCreditCardNumber(Long creditCardNumber) {
    this.creditCardNumber = creditCardNumber;
  }

  public CreditCardStatus getCreditCardStatus() {
    return creditCardStatus;
  }

  public void setCreditCardStatus(CreditCardStatus creditCardStatus) {
    this.creditCardStatus = creditCardStatus;
  }

}
