package asd.group2.bms.payload.request;

import asd.group2.bms.model.leaves.RequestStatus;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;

/**
 * @description: Structure of leave body request
 */
public class UpdateLeaveStatusRequest {

  @NotNull(message = "Leave id is required")
  private Long leaveId;

  @Enumerated(EnumType.STRING)
  private RequestStatus requestStatus;

  public Long getLeaveId() {
    return leaveId;
  }

  public void setLeaveId(Long leaveId) {
    this.leaveId = leaveId;
  }

  public RequestStatus getRequestStatus() {
    return requestStatus;
  }

  public void setRequestStatus(RequestStatus requestStatus) {
    this.requestStatus = requestStatus;
  }

}