package asd.group2.bms.payload.request;

import asd.group2.bms.model.user.AccountStatus;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @description: Structure of update account request
 */
public class UpdateAccountStatusRequest {

  @NotBlank(message = "User email is required")
  @Email(message = "Please enter a valid email")
  private String email;

  @Enumerated(EnumType.STRING)
  @NotNull
  private AccountStatus accountStatus;

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public AccountStatus getAccountStatus() {
    return accountStatus;
  }

  public void setAccountStatus(AccountStatus accountStatus) {
    this.accountStatus = accountStatus;
  }

}
