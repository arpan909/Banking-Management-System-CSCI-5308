package asd.group2.bms.service;

import asd.group2.bms.model.account.Account;
import asd.group2.bms.model.cards.debit.DebitCard;
import asd.group2.bms.model.cards.debit.DebitCardStatus;

public interface IDebitCardService {

  DebitCard createDebitCard(Account account);

  DebitCard getDebitCardByNumber(Long debitCardNumber);

  DebitCard getDebitCardByAccountNumber(Long accountNumber);

  Boolean setDebitCardLimit(Long debitCardNumber, Integer transactionLimit);

  Boolean setDebitCardRequestStatus(Long debitCardNumber,
                                    DebitCardStatus debitCardStatus);

  Boolean setDebitCardPin(Long debitCardNumber, String pin, Long id);

}
