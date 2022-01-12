package asd.group2.bms.service;

import asd.group2.bms.model.account.Account;
import asd.group2.bms.model.cards.credit.CreditCard;
import asd.group2.bms.model.cards.credit.CreditCardStatus;
import asd.group2.bms.payload.response.CreditCardListResponse;
import asd.group2.bms.payload.response.PagedResponse;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;

public interface ICreditCardService {

  PagedResponse<CreditCardListResponse> getCreditCardListByStatus(CreditCardStatus creditCardStatus, int page, int size);

  CreditCard getCreditCardByCreditCardNumber(Long creditCardNumber);

  Boolean setCreditCardRequestStatus(Long creditCardNumber,
                                     CreditCardStatus creditCardStatus) throws MessagingException, UnsupportedEncodingException;

  CreditCard createCreditCard(Account account, Integer requestedTransactionLimit) throws Exception;

  Boolean setCreditCardPin(Long creditCardNumber, String pin, Long id) throws Exception;

}
