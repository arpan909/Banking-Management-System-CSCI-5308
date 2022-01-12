package asd.group2.bms.payload.request;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class CreditCardSetPinRequest {

  @NotNull(message = "Credit card number is required to set the pin")
  private Long creditCardNumber;

  @NotNull(message = "PIN is required")
  @Size(min = 4, max = 6, message = "Enter PIN between 4-6 digits")
  private String pin;

  public Long getCreditCardNumber() {
    return creditCardNumber;
  }

  public void setCreditCardNumber(Long creditCardNumber) {
    this.creditCardNumber = creditCardNumber;
  }

  public String getPin() {
    return pin;
  }

  public void setPin(String pin) {
    this.pin = pin;
  }

}
