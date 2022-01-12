package asd.group2.bms.serviceImpl;

import asd.group2.bms.exception.ResourceNotFoundException;
import asd.group2.bms.model.account.Account;
import asd.group2.bms.model.cards.debit.DebitCard;
import asd.group2.bms.model.cards.debit.DebitCardStatus;
import asd.group2.bms.repository.IDebitCardRepository;
import asd.group2.bms.service.IDebitCardService;
import asd.group2.bms.util.AppConstants;
import asd.group2.bms.util.CardDetails;
import asd.group2.bms.util.Helper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DebitCardServiceImpl implements IDebitCardService {

  @Autowired
  IDebitCardRepository debitCardRepository;

  @Autowired
  Helper helper;

  /**
   * @param account: Account of user whose debit card is being created
   * @return This will return the debit card details
   */
  @Override
  public DebitCard createDebitCard(Account account) {
    CardDetails cardDetails = helper.generateCardDetails();

    String debitCardNumber = cardDetails.getCardNumber();
    String expiryMonth = cardDetails.getExpiryMonth();
    String expiryYear = cardDetails.getExpiryYear();
    String pin = cardDetails.getPin();
    String cvv = cardDetails.getCvv();

    DebitCard debitCard = new DebitCard(Long.parseLong(debitCardNumber),
        account, pin, AppConstants.DEFAULT_TRANSACTION_LIMIT, DebitCardStatus.ACTIVE, expiryYear,
        expiryMonth,
        cvv);

    return debitCardRepository.save(debitCard);
  }

  /**
   * @param debitCardNumber: Debit Card Number
   * @return Returns Debit card of the to change transaction limit
   */
  @Override
  public DebitCard getDebitCardByNumber(Long debitCardNumber) {
    return debitCardRepository.findById(debitCardNumber).orElseThrow(() -> new ResourceNotFoundException("DebitCard Number", "debitCardNumber", debitCardNumber));
  }

  /**
   * @param accountNumber: Account Number
   * @return Returns Debit card of the to change transaction limit
   */
  @Override
  public DebitCard getDebitCardByAccountNumber(Long accountNumber) {
    return debitCardRepository.findByAccountNumber(accountNumber).orElseThrow(() -> new ResourceNotFoundException("DebitCard Number", "debitCardNumber", "Wrong debit card number"));
  }

  /**
   * @param debitCardNumber:  Debit Card Number
   * @param transactionLimit: Transaction limit to set for the given debit card number
   * @return Returns Debit card of the changed transaction limit
   */
  @Override
  public Boolean setDebitCardLimit(Long debitCardNumber, Integer transactionLimit) {
    DebitCard debitCard = getDebitCardByNumber(debitCardNumber);
    debitCard.setTransactionLimit(transactionLimit);
    return debitCardRepository.update(debitCard);
  }

  /**
   * @param debitCardNumber: Debit Card Number
   * @param debitCardStatus: Status of Debit Card
   * @return Returns Debit card of the changed Debit Card status
   */
  @Override
  public Boolean setDebitCardRequestStatus(Long debitCardNumber,
                                           DebitCardStatus debitCardStatus) {
    DebitCard debitCard = getDebitCardByNumber(debitCardNumber);
    debitCard.setDebitCardStatus(debitCardStatus);
    return debitCardRepository.update(debitCard);
  }

  /**
   * @param debitCardNumber: Debit card number
   * @param pin:             Pin to set for the given debit card
   * @return Returns debit card of the changed pin
   */
  @Override
  public Boolean setDebitCardPin(Long debitCardNumber, String pin, Long id) {
    DebitCard debitCard = getDebitCardByNumber(debitCardNumber);
    Long userId = debitCard.getAccount().getUser().getId();
    if (!debitCard.getPin().equals(pin) && userId == id) {
      debitCard.setPin(pin);
      return debitCardRepository.update(debitCard);
    }
    return false;
  }

}
