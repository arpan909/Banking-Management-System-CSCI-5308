package asd.group2.bms.payload.response;

/**
 * @description: This class will be responsible to return availability of the user.
 */
public class UserIdentityAvailability {

  private Boolean available;

  public UserIdentityAvailability(Boolean available) {
    this.available = available;
  }

  public Boolean getAvailable() {
    return available;
  }

  public void setAvailable(Boolean available) {
    this.available = available;
  }

}
