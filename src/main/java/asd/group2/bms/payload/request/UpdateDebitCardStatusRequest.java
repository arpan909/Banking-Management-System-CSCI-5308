package asd.group2.bms.payload.request;

import asd.group2.bms.model.cards.debit.DebitCardStatus;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;

/**
 * Description: Structure of debit card status body request
 */
public class UpdateDebitCardStatusRequest {

  @NotNull(message = "Debit Card Number is required")
  private Long debitCardNumber;

  @Enumerated(EnumType.STRING)
  private DebitCardStatus debitCardStatus;

  public Long getDebitCardNumber() {
    return debitCardNumber;
  }

  public void setDebitCardNumber(Long debitCardNumber) {
    this.debitCardNumber = debitCardNumber;
  }

  public DebitCardStatus getDebitCardStatus() {
    return debitCardStatus;
  }

  public void setDebitCardStatus(DebitCardStatus debitCardStatus) {
    this.debitCardStatus = debitCardStatus;
  }

}
