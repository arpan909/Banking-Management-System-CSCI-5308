package asd.group2.bms.serviceImpl;

import asd.group2.bms.exception.ResourceNotFoundException;
import asd.group2.bms.model.account.Account;
import asd.group2.bms.model.account.AccountActivity;
import asd.group2.bms.model.account.ActivityType;
import asd.group2.bms.model.term_deposit.TermDepositDetail;
import asd.group2.bms.model.term_deposit.TermDepositStatus;
import asd.group2.bms.payload.response.ApiResponse;
import asd.group2.bms.repository.IAccountActivityRepository;
import asd.group2.bms.repository.ITermDepositDetailRepository;
import asd.group2.bms.service.IAccountService;
import asd.group2.bms.service.ICustomEmail;
import asd.group2.bms.service.ITermDepositDetailService;
import asd.group2.bms.util.AppConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class TermDepositDetailServiceImpl implements ITermDepositDetailService {

  @Autowired
  ITermDepositDetailRepository termDepositDetailRepository;

  @Autowired
  IAccountActivityRepository accountActivityRepository;

  @Autowired
  IAccountService accountService;

  @Autowired
  ICustomEmail customEmail;

  @Override
  public ResponseEntity<?> makeTermDepositRequest(Long userId, String email, String firstName, Double fdAmount,
                                                  Date currentDate, int duration) throws Exception {

    try {
      Account account = accountService.getAccountByUserId(userId);
      if (fdAmount < AppConstants.MINIMUM_BALANCE) {
        return new ResponseEntity<>(
            new ApiResponse(false, "Minimum amount to" + " create Fixed Deposit is $" + AppConstants.MINIMUM_BALANCE),
            HttpStatus.BAD_REQUEST);
      }
      if (account.getBalance() < fdAmount) {
        return new ResponseEntity<>(new ApiResponse(false, "Not enough balance in your account!"),
            HttpStatus.BAD_REQUEST);
      }
      if (account.getBalance() - AppConstants.MINIMUM_BALANCE < fdAmount) {
        return new ResponseEntity<>(new ApiResponse(false, "Minimum $" + AppConstants.MINIMUM_BALANCE + " is "
            + "required after creating Fixed Deposit in your account!"), HttpStatus.BAD_REQUEST);
      }

      // Balance Updated
      Double newBalance = account.getBalance() - fdAmount;
      account.setBalance(newBalance);
      Boolean isUpdated = accountService.updateAccountBalance(account);

      if (!isUpdated) {
        return new ResponseEntity<>(new ApiResponse(false, "Something went " + "wrong while updating balance!"),
            HttpStatus.INTERNAL_SERVER_ERROR);
      }

      // Sending email
      customEmail.sendBalanceDeductionMail(email, firstName, fdAmount, newBalance);

      // Maturity Date calculation
      Calendar c = Calendar.getInstance();
      float interestRate = AppConstants.DEFAULT_INTEREST_VALUE;
      c.setTime(currentDate);
      c.add(Calendar.YEAR, duration);
      Date maturityDate = c.getTime();

      // Calculating maturityAmount
      Double maturityAmount = fdAmount * Math.pow((1 + (interestRate / 12)), 12 * duration);

      // New TermDepositDetail
      TermDepositDetail termDepositDetail = new TermDepositDetail(account, currentDate, fdAmount, duration,
          interestRate, maturityDate, maturityAmount, TermDepositStatus.ACTIVE);

      //Setting account activity
      AccountActivity accountActivity = new AccountActivity(account,
          ActivityType.WITHDRAWAL, fdAmount, "Term Deposit withdrawal!");
      accountActivityRepository.save(accountActivity);

      // Saving TermDepositDetail & TermDeposit
      termDepositDetailRepository.save(termDepositDetail);

      return ResponseEntity.ok(new ApiResponse(true, "Term Deposit made successfully!"));
    } catch (Exception e) {
      e.printStackTrace();
      return new ResponseEntity<>(new ApiResponse(false, "Something went " + "wrong!"),
          HttpStatus.INTERNAL_SERVER_ERROR);
    }

  }

  @Override
  public TermDepositDetail getTermDepositDetailById(Long id) {

    return termDepositDetailRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Termdeposit", "termdeposit", "temp"));
  }

  @Override
  public List<TermDepositDetail> getTermDepositDetail(Long userId) {

    Account account = accountService.getAccountByUserId(userId);
    List<TermDepositDetail> termDepositDetailList = termDepositDetailRepository
        .findTermDepositDetailByAccount_AccountNumber(account.getAccountNumber());
    if (termDepositDetailList.size() > 0) {
      return termDepositDetailList;
    }
    return new ArrayList<>();
  }

  /**
   * This method is to close term deposit
   *
   * @param termDepositDetail: Request body of term deposit
   * @return updated termDepositDetail
   */
  @Override
  public Boolean closeTermDepositDetail(TermDepositDetail termDepositDetail) {

    float interestRate = AppConstants.SAVING_INTEREST_VALUE;
    Account account = termDepositDetail.getAccount();
    Date fromDate = termDepositDetail.getStartDate();
    Date toDate = new Date();

    // logic of calculating amount

    String modifiedFromDate = new SimpleDateFormat("yyyy-MM-dd").format(fromDate);
    String modifiedToDate = new SimpleDateFormat("yyyy-MM-dd").format(toDate);

    long duration = ChronoUnit.MONTHS.between(YearMonth.from(LocalDate.parse(modifiedFromDate)),
        YearMonth.from(LocalDate.parse(modifiedToDate)));

    Double maturityAmount = termDepositDetail.getInitialAmount() + Math.pow((1 + (interestRate / 12)), duration);

    // Crediting term deposit money to account
    Double accountBalance = account.getBalance() + maturityAmount;
    account.setBalance(accountBalance);

    accountService.updateAccountBalance(account);

    // changing status of term deposit
    termDepositDetail.setTermDepositStatus(TermDepositStatus.PREMATURE);
    termDepositDetail.setMaturityDate(new Date());

    return termDepositDetailRepository.update(termDepositDetail);
  }

  @Override
  public Boolean checkActiveTermDeposit(List<TermDepositDetail> termDepositDetailList) {
    for (TermDepositDetail termDepositDetail : termDepositDetailList) {
      if (termDepositDetail.getTermDepositStatus() == TermDepositStatus.ACTIVE) {
        return true;
      }
    }
    return false;
  }

}
