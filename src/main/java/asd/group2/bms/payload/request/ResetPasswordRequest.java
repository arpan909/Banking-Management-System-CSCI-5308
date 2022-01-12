package asd.group2.bms.payload.request;

import javax.validation.constraints.NotBlank;

/**
 * @description: Structure of reset password request
 */
public class ResetPasswordRequest {

  @NotBlank(message = "Token is required")
  private String token;

  @NotBlank(message = "New Password is required")
  private String newPassword;

  @NotBlank(message = "Confirm New Password is required")
  private String confirmNewPassword;

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

  public String getNewPassword() {
    return newPassword;
  }

  public void setNewPassword(String newPassword) {
    this.newPassword = newPassword;
  }

  public String getConfirmNewPassword() {
    return confirmNewPassword;
  }

  public void setConfirmNewPassword(String confirmNewPassword) {
    this.confirmNewPassword = confirmNewPassword;
  }

}
