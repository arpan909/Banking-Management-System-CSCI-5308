package asd.group2.bms.model.cheque;

import asd.group2.bms.model.account.Account;
import asd.group2.bms.model.audit.DateAudit;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

/**
 * @description: This will create chequebook table in the database
 */
@Entity
@Table(name = "chequebooks")
public class Chequebook extends DateAudit {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long chequebookNumber;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "account_number", nullable = false)
  @OnDelete(action = OnDeleteAction.CASCADE)
  @JsonIgnore
  private Account account;

  @Column(name = "is_issued", columnDefinition = "boolean default false")
  private Boolean isIssued;

  public Chequebook() {
  }

  public Chequebook(Account account, Boolean isIssued) {
    this.account = account;
    this.isIssued = isIssued;
  }

  public Long getChequebookNumber() {
    return chequebookNumber;
  }

  public void setChequebookNumber(Long chequebookNumber) {
    this.chequebookNumber = chequebookNumber;
  }

  public Account getAccount() {
    return account;
  }

  public void setAccount(Account account) {
    this.account = account;
  }

  public Boolean getIssued() {
    return isIssued;
  }

  public void setIssued(Boolean issued) {
    isIssued = issued;
  }

}
