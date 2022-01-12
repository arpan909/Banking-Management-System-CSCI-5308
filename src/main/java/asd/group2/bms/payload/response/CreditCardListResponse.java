package asd.group2.bms.payload.response;

import asd.group2.bms.model.cards.credit.CreditCardStatus;

/**
 * @description: This class will be responsible to return credit cards list.
 */
public class CreditCardListResponse {

  private Long creditCardNumber;

  private AccountDetailResponse accountDetailResponse;

  private String pin;

  private Integer transactionLimit;

  private CreditCardStatus creditCardStatus;

  private String expiryYear;

  private String expiryMonth;

  private String cvv;

  private Boolean isActive;

  public Long getCreditCardNumber() {
    return creditCardNumber;
  }

  public void setCreditCardNumber(Long creditCardNumber) {
    this.creditCardNumber = creditCardNumber;
  }

  public AccountDetailResponse getAccountDetailResponse() {
    return accountDetailResponse;
  }

  public void setAccountDetailResponse(AccountDetailResponse accountDetailResponse) {
    this.accountDetailResponse = accountDetailResponse;
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
