package asd.group2.bms.model.cards.debit;

import asd.group2.bms.model.account.Account;
import asd.group2.bms.model.audit.DateAudit;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @description: This will create debit cards table in the database
 */
@Entity
@Table(name = "debit_cards")
public class DebitCard extends DateAudit {

  @Id
  private Long debitCardNumber;

  @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
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
  private DebitCardStatus debitCardStatus;

  @NotBlank
  @Size(max = 4)
  private String expiryYear;

  @NotBlank
  @Size(max = 2)
  private String expiryMonth;

  @NotBlank
  @Size(max = 6)
  private String cvv;

  public DebitCard() {
  }

  public DebitCard(Long debitCardNumber, Account account, String pin,
                   Integer transactionLimit,
                   DebitCardStatus debitCardStatus, String expiryYear, String expiryMonth, String cvv) {
    this.debitCardNumber = debitCardNumber;
    this.account = account;
    this.pin = pin;
    this.transactionLimit = transactionLimit;
    this.debitCardStatus = debitCardStatus;
    this.expiryYear = expiryYear;
    this.expiryMonth = expiryMonth;
    this.cvv = cvv;
  }

  public Long getDebitCardNumber() {
    return debitCardNumber;
  }

  public void setDebitCardNumber(Long debitCardNumber) {
    this.debitCardNumber = debitCardNumber;
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

  public DebitCardStatus getDebitCardStatus() {
    return debitCardStatus;
  }

  public void setDebitCardStatus(DebitCardStatus debitCardStatus) {
    this.debitCardStatus = debitCardStatus;
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

}
