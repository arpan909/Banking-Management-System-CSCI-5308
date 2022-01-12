package asd.group2.bms.repository;

import asd.group2.bms.model.cards.credit.CreditCard;
import asd.group2.bms.model.cards.credit.CreditCardStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ICreditCardRepository {

  /**
   * @param creditCardStatus: request status
   * @descriptions: This will return the Credit Card records by status.
   */
  Page<CreditCard> findByCreditCardStatusEquals(CreditCardStatus creditCardStatus, Pageable pageable);

  /**
   * @param creditCardNumber: number of the credit card
   * @return This will return user based on user id.
   */
  Optional<CreditCard> findById(Long creditCardNumber);

  /**
   * @param creditCard: Credit Card details
   * @return This will return creditCard if inserted in the database.
   */
  CreditCard save(CreditCard creditCard);

  /**
   * @param creditCard: Credit Card details
   * @return true if creditCard id updated else false
   */
  Boolean update(CreditCard creditCard);

}
