package asd.group2.bms.payload.request;

import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

/**
 * @description: Structure of update profile request
 */
public class UpdateProfileRequest {

  @NotBlank(message = "First Name is required")
  @Size(min = 3, max = 40)
  private String firstName;

  @NotBlank(message = "Last Name is required")
  @Size(min = 3, max = 40)
  private String lastName;

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

}
