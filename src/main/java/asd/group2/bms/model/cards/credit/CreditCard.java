package asd.group2.bms.model.cards.credit;

import asd.group2.bms.model.account.Account;
import asd.group2.bms.model.audit.DateAudit;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @description: This will create credit cards table in the database
 */
@Entity
@Table(name = "credit_cards")
public class CreditCard extends DateAudit {

  @Id
  private Long creditCardNumber;

  @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
  @JoinColumn(name = "account_number", nullable = false)
  @OnDelete(action = OnDeleteAction.CASCADE)
  private Account account;

  @NotBlank
  @Size(max = 6)
  private String pin;

  @NotNull
  @Column(name = "transaction_limit", columnDefinition = "integer default 5000")
  private Integer transactionLimit;

  @Enumerated(EnumType.STRING)
  @NotNull
  private CreditCardStatus creditCardStatus;

  @NotBlank
  @Size(max = 4)
  private String expiryYear;

  @NotBlank
  @Size(max = 2)
  private String expiryMonth;

  @NotBlank
  @Size(max = 6)
  private String cvv;

  @Column(name = "is_active", columnDefinition = "boolean default false")
  private Boolean isActive;

  public CreditCard() {
  }

  public CreditCard(Long creditCardNumber, Account account, String pin,
                    Integer transactionLimit,
                    CreditCardStatus creditCardStatus, String expiryYear, String expiryMonth, String cvv, Boolean isActive) {
    this.creditCardNumber = creditCardNumber;
    this.account = account;
    this.pin = pin;
    this.transactionLimit = transactionLimit;
    this.creditCardStatus = creditCardStatus;
    this.expiryYear = expiryYear;
    this.expiryMonth = expiryMonth;
    this.cvv = cvv;
    this.isActive = isActive;
  }

  public Long getCreditCardNumber() {
    return creditCardNumber;
  }

  public void setCreditCardNumber(Long creditCardNumber) {
    this.creditCardNumber = creditCardNumber;
  }

  public Account getAccount() {
    return account;
  }

  public void setAccount(Account account) {
    this.account = account;
  }

  public String getPin() {
    return pin;
  }

  public void setPin(String pin) {
    this.pin = pin;
  }

  public Integer getTransactionLimit() {
    return transactionLimit;
  }

  public void setTransactionLimit(Integer transactionLimit) {
    this.transactionLimit = transactionLimit;
  }

  public CreditCardStatus getCreditCardStatus() {
    return creditCardStatus;
  }

  public void setCreditCardStatus(CreditCardStatus creditCardStatus) {
    this.creditCardStatus = creditCardStatus;
  }

  public String getExpiryYear() {
    return expiryYear;
  }

  public void setExpiryYear(String expiryYear) {
    this.expiryYear = expiryYear;
  }

  public String getExpiryMonth() {
    return expiryMonth;
  }

  public void setExpiryMonth(String expiryMonth) {
    this.expiryMonth = expiryMonth;
  }

  public String getCvv() {
    return cvv;
  }

  public void setCvv(String cvv) {
    this.cvv = cvv;
  }

  public Boolean getActive() {
    return isActive;
  }

  public void setActive(Boolean active) {
    isActive = active;
  }

}
