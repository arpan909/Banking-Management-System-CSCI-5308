package asd.group2.bms.serviceImpl;

import asd.group2.bms.exception.ResourceNotFoundException;
import asd.group2.bms.model.account.Account;
import asd.group2.bms.model.cards.credit.CreditCard;
import asd.group2.bms.model.cards.credit.CreditCardStatus;
import asd.group2.bms.payload.response.CreditCardListResponse;
import asd.group2.bms.payload.response.PagedResponse;
import asd.group2.bms.repository.ICreditCardRepository;
import asd.group2.bms.service.ICreditCardService;
import asd.group2.bms.service.ICustomEmail;
import asd.group2.bms.util.CardDetails;
import asd.group2.bms.util.Helper;
import asd.group2.bms.util.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.List;

@Service
public class CreditCardServiceImpl implements ICreditCardService {

  @Autowired
  ICreditCardRepository creditCardRepository;

  @Autowired
  ICustomEmail customEmail;

  @Autowired
  Helper helper;

  /**
   * @param creditCardStatus: Credit Card Status (PENDING, APPROVED, REJECTED)
   * @param page:             Page Number
   * @param size:             Size of the response data
   * @description: This will return all the credit cards having status resignStatus
   */
  @Override
  public PagedResponse<CreditCardListResponse> getCreditCardListByStatus(CreditCardStatus creditCardStatus, int page, int size) {
    // Making list in ascending order
    Pageable pageable = PageRequest.of(page, size, Sort.Direction.ASC, "createdAt");
    Page<CreditCard> cards = creditCardRepository.findByCreditCardStatusEquals(creditCardStatus, pageable);

    if (cards.getNumberOfElements() == 0) {
      return new PagedResponse<>(Collections.emptyList(), cards.getNumber(),
          cards.getSize(), cards.getTotalElements(), cards.getTotalPages(), cards.isLast());
    }

    List<CreditCardListResponse> creditCardListResponses = cards.map(ModelMapper::mapCreditCardToCreditCardListResponse).getContent();

    return new PagedResponse<>(creditCardListResponses, cards.getNumber(),
        cards.getSize(), cards.getTotalElements(), cards.getTotalPages(), cards.isLast());
  }

  /**
   * @param creditCardNumber: credit card number
   * @return credit card based on credit card number
   */
  @Override
  public CreditCard getCreditCardByCreditCardNumber(Long creditCardNumber) {
    return creditCardRepository.findById(creditCardNumber).orElseThrow(() -> new ResourceNotFoundException("Credit Card Number", "creditCardNumber", creditCardNumber));
  }

  /**
   * @param creditCardNumber: credit card number
   * @param creditCardStatus: Status of the credit card (APPROVED, REJECTED, PENDING)
   * @return the updated status of the credit card having credit card number - creditCardNumber
   */
  @Override
  public Boolean setCreditCardRequestStatus(Long creditCardNumber,
                                            CreditCardStatus creditCardStatus
  ) throws MessagingException,
      UnsupportedEncodingException {
    CreditCard creditCard = getCreditCardByCreditCardNumber(creditCardNumber);
    String email = creditCard.getAccount().getUser().getEmail();
    String firstName = creditCard.getAccount().getUser().getFirstName();
    Integer transactionLimit = creditCard.getTransactionLimit();
    if (creditCardStatus == CreditCardStatus.APPROVED) {
      int creditScore = creditCard.getAccount().getCreditScore();
      if (creditScore > 650 && creditScore < 700) {
        if (transactionLimit > 1000) {
          transactionLimit = 1000;
        }
      } else if (creditScore >= 700 && creditScore < 750) {
        if (transactionLimit > 1500) {
          transactionLimit = 1500;
        }
      } else if (creditScore >= 750 && creditScore < 800) {
        if (transactionLimit > 2500) {
          transactionLimit = 2500;
        }
      } else {
        if (transactionLimit > 5000) {
          transactionLimit = 5000;
        }
      }
      creditCard.setTransactionLimit(transactionLimit);
    }
    creditCard.setCreditCardStatus(creditCardStatus);
    boolean response = creditCardRepository.update(creditCard);
    if (response) {
      customEmail.sendCreditCardApprovalMail(email, firstName,
          transactionLimit);

    }
    return response;
  }


  /**
   * @param account: account for which credit card would be created
   * @return This will return the debit card details
   */
  @Override
  public CreditCard createCreditCard(Account account,
                                     Integer requestedTransactionLimit) throws Exception {
    CardDetails cardDetails = helper.generateCardDetails();

    int creditScore = account.getCreditScore();
    if (creditScore > 650) {
      String creditCardNumber = cardDetails.getCardNumber();
      String expiryMonth = cardDetails.getExpiryMonth();
      String expiryYear = cardDetails.getExpiryYear();
      String pin = cardDetails.getPin();
      String cvv = cardDetails.getCvv();

      CreditCard creditCard = new CreditCard(Long.parseLong(creditCardNumber),
          account, pin, requestedTransactionLimit, CreditCardStatus.PENDING, expiryYear, expiryMonth,
          cvv, false);

      return creditCardRepository.save(creditCard);
    } else {
      throw new Exception("You are not eligible to apply for our Credit Card");
    }
  }

  /**
   * @param creditCardNumber : credit card number for which request is made
   * @param pin              : new PIN to be set
   * @param id
   * @return: boolean result
   */
  @Override
  public Boolean setCreditCardPin(Long creditCardNumber, String pin, Long id) throws
      Exception {
    CreditCard creditCard = getCreditCardByCreditCardNumber(creditCardNumber);
    Long userId = creditCard.getAccount().getUser().getId();
    if (userId == id) {
      creditCard.setPin(pin);
      return creditCardRepository.update(creditCard);
    } else {
      throw new Exception("You are not authorized to perform this operation");
    }
  }

}
