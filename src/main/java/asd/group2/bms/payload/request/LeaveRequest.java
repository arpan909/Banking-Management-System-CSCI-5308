package asd.group2.bms.payload.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

public class LeaveRequest {

  @NotNull(message = "From date cant be empty!")
  private Date fromDate;

  @NotNull(message = "To date cant be empty!")
  private Date toDate;

  @NotBlank(message = "Reason cant be empty!")
  private String reason;

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

}
