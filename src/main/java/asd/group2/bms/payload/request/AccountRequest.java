package asd.group2.bms.payload.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @description: Structure of account creation request
 */
public class AccountRequest {

  @NotBlank(message = "User email is required")
  @Email(message = "Please enter a valid email")
  private String email;

  @NotNull
  private Double balance;

  @NotNull
  private int creditScore;

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
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
