package asd.group2.bms.payload.request;

import javax.validation.constraints.NotNull;

public class DebitCardSetPinRequest {

  @NotNull(message = "Debit card number is required to set the pin")
  private Long debitCardNumber;

  @NotNull(message = "Required pin to set the limit")
  private String pin;

  public Long getDebitCardNumber() {
    return debitCardNumber;
  }

  public void setDebitCardNumber(Long debitCardNumber) {
    this.debitCardNumber = debitCardNumber;
  }

  public String getPin() {
    return pin;
  }

  public void setPin(String pin) {
    this.pin = pin;
  }

}
