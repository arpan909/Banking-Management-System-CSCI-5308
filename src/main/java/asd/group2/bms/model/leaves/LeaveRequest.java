package asd.group2.bms.model.leaves;

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
 * @description: This will leave request table in the database
 */
@Entity
@Table(name = "leaves")
public class LeaveRequest extends DateAudit {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long leaveId;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "user_id", nullable = false)
  @OnDelete(action = OnDeleteAction.CASCADE)
  private User user;

  @NotNull
  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private Date fromDate;

  @NotNull
  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private Date toDate;

  @NotBlank
  private String reason;

  @Enumerated(EnumType.STRING)
  @NotNull
  private RequestStatus requestStatus;

  public LeaveRequest() {
  }

  public LeaveRequest(User user, Date fromDate, Date toDate, String reason, RequestStatus requestStatus) {
    this.user = user;
    this.fromDate = fromDate;
    this.toDate = toDate;
    this.reason = reason;
    this.requestStatus = requestStatus;
  }

  public Long getLeaveId() {
    return leaveId;
  }

  public void setLeaveId(Long leaveId) {
    this.leaveId = leaveId;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public Date getFromDate() {
    return fromDate;
  }

  public void setFromDate(Date fromDate) {
    this.fromDate = fromDate;
  }

  public Date getToDate() {
    return toDate;
  }

  public void setToDate(Date toDate) {
    this.toDate = toDate;
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
