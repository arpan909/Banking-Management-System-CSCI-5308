package asd.group2.bms.payload.request;

import javax.validation.constraints.NotNull;

public class FundTransferRequest {

  @NotNull(message = "Username or email is required")
  private Long senderAccountNumber;

  @NotNull(message = "Password can not be empty")
  private Long receiverAccountNumber;

  private String comment;

  @NotNull(message = "Amount can not be null")
  private Double transactionAmount;

  public Long getSenderAccountNumber() {
    return senderAccountNumber;
  }

  public void setSenderAccountNumber(Long senderAccountNumber) {
    this.senderAccountNumber = senderAccountNumber;
  }

  public Long getReceiverAccountNumber() {
    return receiverAccountNumber;
  }

  public void setReceiverAccountNumber(Long receiverAccountNumber) {
    this.receiverAccountNumber = receiverAccountNumber;
  }

  public String getComment() {
    return comment;
  }

  public void setComment(String comment) {
    this.comment = comment;
  }

  public Double getTransactionAmount() {
    return transactionAmount;
  }

  public void setTransactionAmount(Double transactionAmount) {
    this.transactionAmount = transactionAmount;
  }

}
