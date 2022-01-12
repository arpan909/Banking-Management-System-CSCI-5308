package asd.group2.bms.serviceImpl;

import asd.group2.bms.model.account.Account;
import asd.group2.bms.model.account.AccountActivity;
import asd.group2.bms.model.account.ActivityType;
import asd.group2.bms.payload.response.ApiResponse;
import asd.group2.bms.payload.response.ApiResponseWithData;
import asd.group2.bms.repository.IAccountActivityRepository;
import asd.group2.bms.security.UserPrincipal;
import asd.group2.bms.service.IAccountActivityService;
import asd.group2.bms.service.IAccountService;
import asd.group2.bms.service.ICustomEmail;
import asd.group2.bms.util.AppConstants;
import asd.group2.bms.util.Helper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Service
public class AccountActivityServiceImpl implements IAccountActivityService {

  @Autowired
  IAccountService accountService;

  @Autowired
  IAccountActivityRepository accountActivityRepository;

  @Autowired
  ICustomEmail customEmail;

  @Autowired
  Helper helper;

  /**
   * @param senderAccountNumber:   sender's account number
   * @param receiverAccountNumber: receiver's account number
   * @param comment:               comment for this transaction
   * @param transactionAmount:     amount to be transferred to and from
   * @return Success or failure based on execution
   * @throws Exception Any exception that might happen during transaction
   */
  @Override
  public ResponseEntity<?> fundTransfer(Long senderAccountNumber,
                                        Long receiverAccountNumber,
                                        String comment,
                                        Double transactionAmount) throws Exception {
    try {
      if (transactionAmount > AppConstants.MAXIMUM_TRANSACTION_LIMIT) {
        return new ResponseEntity<>(new ApiResponse(false, "Transaction amount " +
            "exceed maximum transaction amount"),
            HttpStatus.BAD_REQUEST);
      }

      if (senderAccountNumber == receiverAccountNumber) {
        return new ResponseEntity<>(new ApiResponse(false, "Sender and " +
            "receiver account must be different"),
            HttpStatus.BAD_REQUEST);
      }

      Account senderAccount =
          accountService.getAccountByAccountNumber(senderAccountNumber);

      Account receiverAccount =
          accountService.getAccountByAccountNumber(receiverAccountNumber);


      Double receiverNewBalance =
          receiverAccount.getBalance() + transactionAmount;
      Double senderNewBalance = senderAccount.getBalance() - transactionAmount;

      receiverAccount.setBalance(receiverNewBalance);
      senderAccount.setBalance(senderNewBalance);

      if (senderNewBalance < AppConstants.MINIMUM_BALANCE) {
        return new ResponseEntity<>(new ApiResponse(false, "Not enough balance in your account!"),
            HttpStatus.BAD_REQUEST);
      }

      Boolean isBalanceCredited =
          accountService.updateAccountBalance(receiverAccount);

      Boolean isBalanceDebited =
          accountService.updateAccountBalance(senderAccount);

      if (!isBalanceCredited) {
        throw new Exception("Balance was not credited to receiver's account");
      }

      if (!isBalanceDebited) {
        throw new Exception("Balance was not debited from sender's account");
      }

      AccountActivity senderAccountActivity =
          new AccountActivity(senderAccount, ActivityType.WITHDRAWAL,
              transactionAmount, comment);

      AccountActivity receiverAccountActivity =
          new AccountActivity(receiverAccount, ActivityType.DEPOSIT,
              transactionAmount, comment);

      try {
        AccountActivity senderActivity =
            accountActivityRepository.save(senderAccountActivity);
        AccountActivity receiverActivity =
            accountActivityRepository.save(receiverAccountActivity);

        String senderEmail = senderAccount.getUser().getEmail();
        String senderFirstName = senderAccount.getUser().getFirstName();
        String receiverEmail = receiverAccount.getUser().getEmail();
        String receiverFirstName = receiverAccount.getUser().getFirstName();

        customEmail.sendAccountActivityMail(senderEmail, senderFirstName,
            transactionAmount, senderNewBalance, "debited", senderActivity.getActivityId());

        customEmail.sendAccountActivityMail(receiverEmail, receiverFirstName,
            transactionAmount, receiverNewBalance, "credited",
            receiverActivity.getActivityId());

        return ResponseEntity.ok(new ApiResponse(true, "Funds transferred " +
            "successfully"));
      } catch (Exception e) {
        throw new Exception("Error occurred while transferring funds");
      }
    } catch (Exception e) {
      return new ResponseEntity<>(new ApiResponse(false, "Something went " +
          "wrong while transferring funds! please check details properly"),
          HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  /**
   * @param currentUser: Current logged in user
   * @param fromDate:    From date
   * @param toDate:      To date
   * @return Account statement data to the user
   */
  @Override
  public ResponseEntity<?> getAccountActivity(UserPrincipal currentUser,
                                              Date fromDate,
                                              Date toDate) {
    try {
      if (toDate.before(fromDate)) {
        return new ResponseEntity<>(new ApiResponseWithData(false, "TO DATE " +
            "can not be set before the FROM DATE", Collections.emptyList()),
            HttpStatus.BAD_REQUEST);
      }

      String modifiedFromDate =
          new SimpleDateFormat("yyyy-MM-dd").format(fromDate);
      String modifiedToDate =
          new SimpleDateFormat("yyyy-MM-dd").format(toDate);

      long monthsBetween = ChronoUnit.MONTHS.between(
          YearMonth.from(LocalDate.parse(modifiedFromDate)),
          YearMonth.from(LocalDate.parse(modifiedToDate))
      );

      if (monthsBetween >= AppConstants.MAX_STATEMENT_RETRIEVE_MONTHS) {
        return new ResponseEntity<>(new ApiResponseWithData(false, "Statement" +
            " with period more than " + AppConstants.MAX_STATEMENT_RETRIEVE_MONTHS + " months can not " +
            "retrieved", Collections.emptyList()), HttpStatus.BAD_REQUEST);
      }

      Long userId = currentUser.getId();
      Account account = accountService.getAccountByUserId(userId);
      Long accountNumber = account.getAccountNumber();

      List<AccountActivity> accountActivities =
          accountActivityRepository.findAccountActivityByAccountNumber(accountNumber, fromDate, toDate);

      return ResponseEntity.ok(new ApiResponseWithData(false, "Account " +
          "Statement fetched successfully", accountActivities));
    } catch (Exception e) {
      return new ResponseEntity<>(new ApiResponseWithData(false, "Something went " +
          "wrong while generating account statement", Collections.emptyList()),
          HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

}
