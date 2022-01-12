package asd.group2.bms.repository;

import asd.group2.bms.model.cards.credit.CreditCardBill;

import java.util.Optional;

public interface ICreditCardBillRepository {

  /**
   * @param accountNumber: accountNumber of User
   * @descriptions: This will return the Credit Card Number of the users
   * Card.
   */
  Long getCreditCardNumber(Long accountNumber);

  /**
   * @param creditCardNumber: creditCardNumber of the user
   * @descriptions: This will return the BillAmount of the users
   * Card.
   */

  double getBillAmount(Long creditCardNumber);

  /**
   * @param billId: Bill ID of the users Bill
   * @descriptions: This will return the status whether the bill is paid
   * or not.
   */

  Boolean payCreditCardBill(Long billId);

  /**
   * @param creditCardNo: CreditCardNo of The User
   * @descriptions: This will return the Bills associated with the Creditcard
   * of the user.
   */

  Optional<CreditCardBill> showBills(Long creditCardNo);

}
