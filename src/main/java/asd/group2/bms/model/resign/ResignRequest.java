package asd.group2.bms.model.resign;

import asd.group2.bms.model.audit.DateAudit;
import asd.group2.bms.model.user.User;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @description: This will create resign request table in the database
 */
@Entity
@Table(name = "resigns")
public class ResignRequest extends DateAudit {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long resignId;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "user_id", nullable = false)
  @OnDelete(action = OnDeleteAction.CASCADE)
  private User user;

  @NotNull
  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private Date date;

  @NotBlank
  private String reason;

  @Enumerated(EnumType.STRING)
  @NotNull
  private RequestStatus requestStatus;

  public ResignRequest() {
  }

  public ResignRequest(User user, Date date, String reason, RequestStatus requestStatus) {
    this.user = user;
    this.date = date;
    this.reason = reason;
    this.requestStatus = requestStatus;
  }

  public Long getResignId() {
    return resignId;
  }

  public void setResignId(Long resignId) {
    this.resignId = resignId;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public Date getDate() {
    return date;
  }

  public void setDate(Date date) {
    this.date = date;
  }

  public String getReason() {
    return reason;
  }

  public void setReason(String reason) {
    this.reason = reason;
  }

  public RequestStatus getRequestStatus() {
    return requestStatus;
  }

  public void setRequestStatus(RequestStatus requestStatus) {
    this.requestStatus = requestStatus;
  }

}

