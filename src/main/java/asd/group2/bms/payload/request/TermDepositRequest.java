package asd.group2.bms.payload.request;

import javax.validation.constraints.NotNull;

public class TermDepositRequest {

  @NotNull
  private Double initialAmount;

  @NotNull
  private int years;

  public TermDepositRequest(Double initialAmount, int years) {
    this.initialAmount = initialAmount;
    this.years = years;
  }

  public Double getInitialAmount() {
    return initialAmount;
  }

  public void setInitialAmount(Double initialAmount) {
    this.initialAmount = initialAmount;
  }

  public int getYears() {
    return years;
  }

  public void setYears(int years) {
    this.years = years;
  }

}
