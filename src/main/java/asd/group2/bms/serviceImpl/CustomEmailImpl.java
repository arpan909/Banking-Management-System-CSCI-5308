package asd.group2.bms.serviceImpl;

import asd.group2.bms.service.ICustomEmail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;

@Service
public class CustomEmailImpl implements ICustomEmail {

  @Autowired
  JavaMailSender javaMailSender;

  @Override
  public void sendResetPasswordEmail(String email, String forgotPasswordLink) throws MessagingException, UnsupportedEncodingException {
    MimeMessage message = javaMailSender.createMimeMessage();
    MimeMessageHelper helper = new MimeMessageHelper(message);

    helper.setFrom("dalbank07@gmail.com", "Group 2 Bank");
    helper.setTo(email);

    String subject = "Reset Password Link";
    String content = "<p>Hello Customer,</p>" +
        "<p>You have requested to reset the password</p>" +
        "<p>Click below link to reset your password</p>" +
        "<b><a href=" + forgotPasswordLink + ">Reset my password</a></b>" +
        "<p>Ignore this email if you remember your password or you have not made this request</p>";
    helper.setSubject(subject);
    helper.setText(content, true);
    javaMailSender.send(message);
  }

  @Override
  public void sendAccountCreationMail(String email, String firstName) throws MessagingException, UnsupportedEncodingException {
    MimeMessage message = javaMailSender.createMimeMessage();
    MimeMessageHelper helper = new MimeMessageHelper(message);

    helper.setFrom("dalbank07@gmail.com", "Group 2 Bank Account Team");
    helper.setTo(email);

    String subject = "Successful Account Creation";
    String content = "<p>Dear " + firstName + ",</p>" +
        "<p>We have successfully created your account.</p>" +
        "<p>You can use your login credentials to utilise our banking services.</p>" +
        "<p>Thank you.</p>" +
        "<p>Happy Banking!</p>";
    helper.setSubject(subject);
    helper.setText(content, true);
    javaMailSender.send(message);
  }

  @Override
  public void sendUserAccountStatusChangeMail(String email, String firstName, String accountStatus) throws MessagingException, UnsupportedEncodingException {
    MimeMessage message = javaMailSender.createMimeMessage();
    MimeMessageHelper helper = new MimeMessageHelper(message);

    helper.setFrom("dalbank07@gmail.com", "Group 2 Bank Account Team");
    helper.setTo(email);

    String subject = "Account Status Changed";
    String content = "<p>Dear " + firstName + ",</p>" +
        "<p>Your account status has been changed.</p>" +
        "<p>Current status: " + accountStatus + ".</p>" +
        "<p>If you think this is a mistake, email us at dalbank07@gmail.com</p>" +
        "<p>Thank you.</p>" +
        "<p>Happy Banking!</p>";
    helper.setSubject(subject);
    helper.setText(content, true);
    javaMailSender.send(message);
  }

  @Override
  public void sendDebitCardGenerationMail(String email, String firstName, Long debitCardNumber, String debitCardPin, String expiryMonth, String expiryYear, String cvv) throws MessagingException, UnsupportedEncodingException {
    MimeMessage message = javaMailSender.createMimeMessage();
    MimeMessageHelper helper = new MimeMessageHelper(message);

    helper.setFrom("dalbank07@gmail.com", "Group 2 Bank Account Team");
    helper.setTo(email);

    String subject = "Debit Card Generated";
    String content = "<p>Dear " + firstName + ",</p>" +
        "<p>Your debit card is generated successfully</p>" +
        "<p>Debit Card Number: " + debitCardNumber + ".</p>" +
        "<p>Debit Card Pin: " + debitCardPin + ".</p>" +
        "<p>Debit Card Expiry Month: " + expiryMonth + ".</p>" +
        "<p>Debit Card Expiry Year: " + expiryYear + ".</p>" +
        "<p>Debit Card CVV: " + cvv + ".</p>" +
        "<p>Thank you.</p>" +
        "<p>Happy Banking!</p>";
    helper.setSubject(subject);
    helper.setText(content, true);
    javaMailSender.send(message);
  }

  @Override
  public void sendBalanceDeductionMail(String email, String firstName, Double debitedAmount, Double newBalance) throws MessagingException, UnsupportedEncodingException {
    MimeMessage message = javaMailSender.createMimeMessage();
    MimeMessageHelper helper = new MimeMessageHelper(message);

    helper.setFrom("dalbank07@gmail.com", "Group 2 Bank Account Team");
    helper.setTo(email);

    String subject = "Activity in your account: Amount debited!";
    String content = "<p>Dear " + firstName + ",</p>" +
        "<p>Your balance has been deducted</p>" +
        "<p>Amount debited: " + debitedAmount + "</p>" +
        "<p>Your available balance: " + newBalance + "</p>" +
        "<p>Happy Banking!</p>";
    helper.setSubject(subject);
    helper.setText(content, true);
    javaMailSender.send(message);
  }

  @Override
  public void sendAccountActivityMail(String email, String firstName, Double amount, Double newBalance, String activityType, Long refId) throws MessagingException, UnsupportedEncodingException {
    MimeMessage message = javaMailSender.createMimeMessage();
    MimeMessageHelper helper = new MimeMessageHelper(message);

    helper.setFrom("dalbank07@gmail.com", "Group 2 Bank Account Team");
    helper.setTo(email);

    String subject = "Amount " + activityType + "!";
    String content = "<p>Dear " + firstName + ",</p>" +
        "<p>Amount " + activityType + ": " + amount + "</p>" +
        "<p>Your available balance: " + newBalance + "</p>" +
        "<p>Reference id for this transaction: " + refId + "</p>" +
        "<p>Happy Banking!</p>";
    helper.setSubject(subject);
    helper.setText(content, true);
    javaMailSender.send(message);

  }

  @Override
  public void sendCreditCardApprovalMail(String email, String firstName, Integer transactionLimit) throws MessagingException, UnsupportedEncodingException {
    MimeMessage message = javaMailSender.createMimeMessage();
    MimeMessageHelper helper = new MimeMessageHelper(message);

    helper.setFrom("dalbank07@gmail.com", "Group 2 Bank Account Team");
    helper.setTo(email);

    String subject = "Credit Card Approved";
    String content = "<p>Dear " + firstName + ",</p>" +
        "<p>Your request for credit card has been approved.</p>" +
        "<p>Your transaction limit is " + transactionLimit + "</p>" +
        "<p>Happy Banking!</p>";
    helper.setSubject(subject);
    helper.setText(content, true);
    javaMailSender.send(message);
  }

}
