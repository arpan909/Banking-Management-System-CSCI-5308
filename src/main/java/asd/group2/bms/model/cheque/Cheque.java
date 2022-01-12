package asd.group2.bms.model.cheque;

import asd.group2.bms.model.audit.DateAudit;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

/**
 * @description: This will create cheque table in the database
 */
@Entity
@Table(name = "cheques")
public class Cheque extends DateAudit {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long chequeNumber;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "chequebook_number", nullable = false)
  @OnDelete(action = OnDeleteAction.CASCADE)
  @JsonIgnore
  private Chequebook chequebook;

  public Cheque() {
  }

  public Cheque(Chequebook chequebook) {
    this.chequebook = chequebook;
  }

  public Long getChequeNumber() {
    return chequeNumber;
  }

  public void setChequeNumber(Long chequeNumber) {
    this.chequeNumber = chequeNumber;
  }

  public Chequebook getChequebook() {
    return chequebook;
  }

  public void setChequebook(Chequebook chequebook) {
    this.chequebook = chequebook;
  }

}
