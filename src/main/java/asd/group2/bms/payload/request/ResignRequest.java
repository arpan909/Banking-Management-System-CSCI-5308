package asd.group2.bms.payload.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @description: Structure of resign request
 */
public class ResignRequest {

  @NotBlank(message = "Reason is required")
  private String reason;

  @NotNull(message = "Date is required")
  private Date date;

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

}
