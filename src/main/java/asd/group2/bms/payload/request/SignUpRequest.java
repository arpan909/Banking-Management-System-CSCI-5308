package asd.group2.bms.payload.request;

import asd.group2.bms.model.account.AccountType;
import asd.group2.bms.model.user.RoleType;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

/**
 * @description: Structure of signup request
 */
public class SignUpRequest {

  @NotBlank(message = "First Name is required")
  @Size(min = 3, max = 40)
  private String firstName;

  @NotBlank(message = "Last Name is required")
  @Size(min = 3, max = 40)
  private String lastName;

  @NotBlank(message = "Username is required")
  @Size(min = 3, max = 15)
  private String username;

  @NotBlank(message = "Email is required")
  @Size(max = 40)
  @Email(message = "Please enter a valid email")
  private String email;

  @NotNull(message = "Birthdate is required")
  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private Date birthday;

  @NotBlank(message = "Phone number is required")
  @Size(max = 15)
  private String phone;

  @NotBlank(message = "Address is required")
  @Size(max = 200)
  private String address;

  @NotBlank(message = "City is required")
  @Size(max = 50)
  private String city;

  @NotBlank(message = "State is required")
  @Size(max = 50)
  private String state;

  @NotBlank(message = "Zip Code is required")
  @Size(min = 6, max = 6)
  private String zipCode;

  @NotBlank(message = "Password is required")
  @Size(min = 6, max = 20)
  private String password;

  @Enumerated(EnumType.STRING)
  private AccountType requestedAccountType;

  @Enumerated(EnumType.STRING)
  private RoleType role;

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public Date getBirthday() {
    return birthday;
  }

  public void setBirthday(Date birthday) {
    this.birthday = birthday;
  }

  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public String getState() {
    return state;
  }

  public void setState(String state) {
    this.state = state;
  }

  public String getZipCode() {
    return zipCode;
  }

  public void setZipCode(String zipCode) {
    this.zipCode = zipCode;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public AccountType getRequestedAccountType() {
    return requestedAccountType;
  }

  public void setRequestedAccountType(AccountType requestedAccountType) {
    this.requestedAccountType = requestedAccountType;
  }

  public RoleType getRole() {
    return role;
  }

  public void setRole(RoleType role) {
    this.role = role;
  }

}
