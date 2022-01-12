package asd.group2.bms.repository;

import asd.group2.bms.model.cards.debit.DebitCard;

import java.util.Optional;

public interface IDebitCardRepository {

  /**
   * @param debitCardNumber: number of the debit card
   * @return This will return debit card based on card number.
   */
  Optional<DebitCard> findById(Long debitCardNumber);

  /**
   * @param accountNumber: account number of user
   * @return This will return debit card based on account number.
   */
  Optional<DebitCard> findByAccountNumber(Long accountNumber);

  /**
   * @param debitCard: Debit Card details
   * @return This will return debitCard if inserted in the database.
   */
  DebitCard save(DebitCard debitCard);

  /**
   * @param debitCard: Debit Card details
   * @return true if debitCard id updated else false
   */
  Boolean update(DebitCard debitCard);

}
