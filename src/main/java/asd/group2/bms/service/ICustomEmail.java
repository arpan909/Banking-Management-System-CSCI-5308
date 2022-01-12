package asd.group2.bms.service;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;

public interface ICustomEmail {

  void sendResetPasswordEmail(String email, String forgotPasswordLink) throws MessagingException, UnsupportedEncodingException;

  void sendAccountCreationMail(String email, String firstName) throws MessagingException, UnsupportedEncodingException;

  void sendUserAccountStatusChangeMail(String email, String firstName,
                                       String accountStatus) throws MessagingException, UnsupportedEncodingException;

  void sendDebitCardGenerationMail(String email, String firstName,
                                   Long debitCardNumber, String debitCardPin, String expiryMonth, String expiryYear, String cvv) throws MessagingException, UnsupportedEncodingException;

  void sendBalanceDeductionMail(String email, String firstName,
                                Double debitedAmount, Double newBalance) throws MessagingException, UnsupportedEncodingException;

  void sendAccountActivityMail(String email, String firstName,
                               Double amount,
                               Double newBalance, String activityType,
                               Long refId) throws MessagingException,
      UnsupportedEncodingException;

  void sendCreditCardApprovalMail(String email, String firstName,
                                  Integer transactionLimit) throws MessagingException, UnsupportedEncodingException;

}
