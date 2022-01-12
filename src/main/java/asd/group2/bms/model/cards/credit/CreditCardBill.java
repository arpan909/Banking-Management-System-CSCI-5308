package asd.group2.bms.model.cards.credit;

import asd.group2.bms.model.audit.DateAudit;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @description: This will create credit card bills table in the database
 */
@Entity
@Table(name = "credit_card_bills")
public class CreditCardBill extends DateAudit {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long billId;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "credit_card_number", nullable = false)
  @OnDelete(action = OnDeleteAction.CASCADE)
  private CreditCard creditCard;

  @NotNull
  private Double amount;

  @NotNull
  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private Date dueDate;

  @Enumerated(EnumType.STRING)
  @NotNull
  private BillStatus billStatus;

  public CreditCardBill() {
  }

  public CreditCardBill(CreditCard creditCard, Double amount, Date dueDate, BillStatus billStatus) {
    this.creditCard = creditCard;
    this.amount = amount;
    this.dueDate = dueDate;
    this.billStatus = billStatus;
  }

  public Long getBillId() {
    return billId;
  }

  public void setBillId(Long billId) {
    this.billId = billId;
  }

  public CreditCard getCreditCard() {
    return creditCard;
  }

  public void setCreditCard(CreditCard creditCard) {
    this.creditCard = creditCard;
  }

  public Double getAmount() {
    return amount;
  }

  public void setAmount(Double amount) {
    this.amount = amount;
  }

  public Date getDueDate() {
    return dueDate;
  }

  public void setDueDate(Date dueDate) {
    this.dueDate = dueDate;
  }

  public BillStatus getBillStatus() {
    return billStatus;
  }

  public void setBillStatus(BillStatus billStatus) {
    this.billStatus = billStatus;
  }

}
