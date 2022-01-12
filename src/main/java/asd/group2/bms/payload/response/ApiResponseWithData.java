package asd.group2.bms.payload.response;

import java.util.List;

public class ApiResponseWithData<T> {

  private Boolean success;

  private String message;

  private List<T> data;

  public ApiResponseWithData(Boolean success, String message, List<T> data) {
    this.success = success;
    this.message = message;
    this.data = data;
  }

  public Boolean getSuccess() {
    return success;
  }

  public void setSuccess(Boolean success) {
    this.success = success;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public List<T> getData() {
    return data;
  }

  public void setData(List<T> data) {
    this.data = data;
  }

}
