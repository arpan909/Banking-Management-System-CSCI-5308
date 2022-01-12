package asd.group2.bms.payload.response;

import asd.group2.bms.model.leaves.RequestStatus;

import java.util.Date;

/**
 * @description: Structure of leave list response
 */
public class LeaveListResponse {

  private Long leaveId;

  private Date fromDate;

  private Date toDate;

  private String reason;

  private UserMetaResponse userMetaResponse;

  private RequestStatus requestStatus;

  public Long getLeaveId() {
    return leaveId;
  }

  public void setLeaveId(Long leaveId) {
    this.leaveId = leaveId;
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

  public UserMetaResponse getUserMetaResponse() {
    return userMetaResponse;
  }

  public void setUserMetaResponse(UserMetaResponse userMetaResponse) {
    this.userMetaResponse = userMetaResponse;
  }

  public RequestStatus getRequestStatus() {
    return requestStatus;
  }

  public void setRequestStatus(RequestStatus requestStatus) {
    this.requestStatus = requestStatus;
  }

}