package asd.group2.bms.payload.response;

import asd.group2.bms.model.resign.RequestStatus;

import java.util.Date;

/**
 * @description: This class will be responsible to return resignation list.
 */
public class ResignListResponse {

  private Long resignId;

  private Date date;

  private String reason;

  private RequestStatus requestStatus;

  private UserMetaResponse userMetaResponse;


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

  public UserMetaResponse getUserMetaResponse() {
    return userMetaResponse;
  }

  public void setUserMetaResponse(UserMetaResponse userMetaResponse) {
    this.userMetaResponse = userMetaResponse;
  }

}
