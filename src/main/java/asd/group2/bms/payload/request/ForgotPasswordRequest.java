package asd.group2.bms.payload.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * @description: Structure of forgot password request
 */
public class ForgotPasswordRequest {

  @NotBlank(message = "Email is required")
  @Size(max = 40)
  @Email(message = "Please enter a valid email")
  private String email;

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

}
