package asd.group2.bms.model.user;

import asd.group2.bms.model.account.AccountType;
import asd.group2.bms.model.audit.DateAudit;
import org.hibernate.annotations.NaturalId;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * @description: This will create users table in the database
 */
@Entity
@Table(name = "users", uniqueConstraints = {@UniqueConstraint(columnNames = {"username"}),
    @UniqueConstraint(columnNames = {"email"})})

public class User extends DateAudit {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotBlank
  @Size(max = 40)
  private String firstName;

  @NotBlank
  @Size(max = 40)
  private String lastName;

  @NotBlank
  @Size(max = 15)
  private String username;

  @NaturalId
  @NotBlank
  @Size(max = 40)
  @Email
  private String email;

  @NotNull
  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private Date birthday;

  @NotBlank
  @Size(max = 15)
  private String phone;

  @NotBlank
  @Size(max = 100)
  private String password;

  @NotBlank
  @Size(max = 200)
  private String address;

  @NotBlank
  @Size(max = 50)
  private String city;

  @NotBlank
  @Size(max = 50)
  private String state;

  @NotBlank
  @Size(min = 6, max = 6)
  private String zipCode;

  private AccountStatus accountStatus;

  @Enumerated(EnumType.STRING)
  private AccountType requestedAccountType;

  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
  private Set<Role> roles = new HashSet<>();

  private String forgotPasswordToken;


  public User() {

  }

  public User(String firstName, String lastName, String username, String email, Date birthday, String phone, String password, String address, String city, String state, String zipCode, AccountStatus accountStatus, AccountType requestedAccountType) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.username = username;
    this.email = email;
    this.birthday = birthday;
    this.phone = phone;
    this.password = password;
    this.address = address;
    this.city = city;
    this.state = state;
    this.zipCode = zipCode;
    this.accountStatus = accountStatus;
    this.requestedAccountType = requestedAccountType;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

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

  public Date getBirthday() {
    return birthday;
  }

  public void setBirthday(Date birthday) {
    this.birthday = birthday;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
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

  public AccountStatus getAccountStatus() {
    return accountStatus;
  }

  public void setAccountStatus(AccountStatus accountStatus) {
    this.accountStatus = accountStatus;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public Set<Role> getRoles() {
    return roles;
  }

  public void setRoles(Set<Role> roles) {
    this.roles = roles;
  }

  public String getForgotPasswordToken() {
    return forgotPasswordToken;
  }

  public void setForgotPasswordToken(String forgotPasswordToken) {
    this.forgotPasswordToken = forgotPasswordToken;
  }

  public AccountType getRequestedAccountType() {
    return requestedAccountType;
  }

  public void setRequestedAccountType(AccountType requestedAccountType) {
    this.requestedAccountType = requestedAccountType;
  }

}