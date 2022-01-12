package asd.group2.bms.payload.request;

import asd.group2.bms.model.resign.RequestStatus;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;

/**
 * @description: Structure of resign body request
 */
public class UpdateResignStatusRequest {

  @NotNull(message = "Resign id is required")
  private Long resignId;

  @Enumerated(EnumType.STRING)
  private RequestStatus requestStatus;

  public Long getResignId() {
    return resignId;
  }

  public void setResignId(Long resignId) {
    this.resignId = resignId;
  }

  public RequestStatus getRequestStatus() {
    return requestStatus;
  }

  public void setRequestStatus(RequestStatus requestStatus) {
    this.requestStatus = requestStatus;
  }

}