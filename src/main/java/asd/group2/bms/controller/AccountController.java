package asd.group2.bms.controller;

import asd.group2.bms.model.account.Account;
import asd.group2.bms.model.account.AccountActivity;
import asd.group2.bms.model.account.AccountType;
import asd.group2.bms.model.account.ActivityType;
import asd.group2.bms.model.user.AccountStatus;
import asd.group2.bms.model.user.User;
import asd.group2.bms.payload.request.AccountRequest;
import asd.group2.bms.payload.request.DepositWithdrawalRequest;
import asd.group2.bms.payload.response.AccountDetailResponse;
import asd.group2.bms.payload.response.ApiResponse;
import asd.group2.bms.payload.response.PagedResponse;
import asd.group2.bms.repository.IUserRepository;
import asd.group2.bms.repositoryImpl.AccountActivityRepositoryImpl;
import asd.group2.bms.security.CurrentLoggedInUser;
import asd.group2.bms.security.UserPrincipal;
import asd.group2.bms.service.IAccountService;
import asd.group2.bms.service.ICustomEmail;
import asd.group2.bms.service.IDebitCardService;
import asd.group2.bms.service.IUserService;
import asd.group2.bms.util.AppConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.mail.MessagingException;
import javax.validation.Valid;
import java.io.UnsupportedEncodingException;

@RestController
@RequestMapping("/api")
public class AccountController {

  @Autowired
  IAccountService accountService;

  @Autowired
  IUserService userService;

  @Autowired
  IUserRepository userRepository;

  @Autowired
  ICustomEmail customEmail;

  @Autowired
  IDebitCardService debitCardService;

  @Autowired
  AccountActivityRepositoryImpl accountActivityRepository;

  /**
   * Users list based on the account status of the users
   *
   * @param accountStatus: ACTIVE, REJECTED, PENDING, CLOSED
   * @param page:          number of the page
   * @param size:          page size
   * @return User data of size N having page number "page"
   */
  @GetMapping("/account/user")
  @RolesAllowed({"ROLE_MANAGER", "ROLE_EMPLOYEE"})
  public PagedResponse<User> getUserAccountListByStatus(@RequestParam(value = "accountStatus") AccountStatus accountStatus, @RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page, @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size) {

    return accountService.getUserAccountListByStatus(accountStatus, page, size);
  }

  /**
   * This will fetch user account based on login detail (jwt)
   *
   * @param currentUser: current logged in user
   * @return account detail of logged in user
   */
  @GetMapping("/account/me")
  @RolesAllowed({"ROLE_USER"})
  public AccountDetailResponse getAccountDetails(@CurrentLoggedInUser UserPrincipal currentUser) {

    return accountService.getAccountDetails(currentUser);
  }

  /**
   * This will create user account based on the payload received (balance, credit score)
   *
   * @param accountRequest: Request body containing all necessary data
   * @return success or failure response with appropriate message
   * @throws MessagingException:           This will throw MessagingException
   * @throws UnsupportedEncodingException: This will throw UnsupportedEncodingException
   */
  @PostMapping("/account/user")
  @RolesAllowed({"ROLE_MANAGER", "ROLE_EMPLOYEE"})
  public ResponseEntity<?> createUserAccount(@Valid @RequestBody AccountRequest accountRequest) throws MessagingException, UnsupportedEncodingException {

    String email = accountRequest.getEmail();
    Double balance = accountRequest.getBalance();
    int creditScore = accountRequest.getCreditScore();
    if (!userRepository.existsByEmail(email)) {
      return new ResponseEntity<>(new ApiResponse(false, "User does not exist!"), HttpStatus.BAD_REQUEST);
    }
    Boolean isUpdated = userService.setUserAccountStatus(email, AccountStatus.ACTIVE);
    User user = userService.getUserByEmail(email);
    if (isUpdated) {
      AccountType accountType = user.getRequestedAccountType();
      accountService.createAccount(user, accountType, balance, creditScore);
      try {
        customEmail.sendAccountCreationMail(email, user.getFirstName());
        return ResponseEntity.ok(new ApiResponse(true, "Account created successfully"));
      } catch (MessagingException | UnsupportedEncodingException e) {
        return ResponseEntity.ok(new ApiResponse(true, "Account created successfully"));
      }
    }
    return new ResponseEntity<>(new ApiResponse(false, "Something went wrong while changing account status!"), HttpStatus.BAD_REQUEST);

  }

  /**
   * This will deposit the given money to the account
   *
   * @param depositWithdrawalRequest Request body containing all necessary data
   * @return success or failure response with appropriate message
   * @throws MessagingException           This will throw MessagingException
   * @throws UnsupportedEncodingException This will throw UnsupportedEncodingException
   */
  @PostMapping("/account/user/deposit")
  @RolesAllowed({"ROLE_MANAGER", "ROLE_EMPLOYEE"})
  public ResponseEntity<?> depositMoney(@Valid @RequestBody DepositWithdrawalRequest depositWithdrawalRequest) throws MessagingException, UnsupportedEncodingException {

    Long accountNumber = depositWithdrawalRequest.getAccountNumber();
    Double deposit = depositWithdrawalRequest.getBalance();
    Account account = accountService.getAccountByAccountNumber(accountNumber);
    Double balance = account.getBalance();

    Double updatedBalance = balance + deposit;
    account.setBalance(updatedBalance);
    Boolean isUpdated = accountService.updateAccountBalance(account);

    if (isUpdated) {
      AccountActivity accountActivity = new AccountActivity(account,
          ActivityType.DEPOSIT, deposit, "Money Deposited Successfully.");
      accountActivityRepository.save(accountActivity);
      return new ResponseEntity<>(new ApiResponse(true, "Money deposited successfully."), HttpStatus.OK);
    } else {
      return new ResponseEntity<>(new ApiResponse(false, "Money deposit not successful."), HttpStatus.BAD_REQUEST);
    }
  }

  /**
   * This will withdraw the given amount from account
   *
   * @param depositWithdrawalRequest Request body containing all necessary data
   * @return success or failure response with appropriate message
   * @throws MessagingException           This will throw MessagingException
   * @throws UnsupportedEncodingException This will throw UnsupportedEncodingException
   */
  @PostMapping("/account/user/withdrawal")
  @RolesAllowed({"ROLE_MANAGER", "ROLE_EMPLOYEE"})
  public ResponseEntity<?> withdrawalMoney(@Valid @RequestBody DepositWithdrawalRequest depositWithdrawalRequest) throws MessagingException, UnsupportedEncodingException {

    Long accountNumber = depositWithdrawalRequest.getAccountNumber();
    Double withdrawal = depositWithdrawalRequest.getBalance();
    Account account = accountService.getAccountByAccountNumber(accountNumber);
    Double balance = account.getBalance();

    if (withdrawal > balance) {

      return new ResponseEntity<>(new ApiResponse(false, "Insufficient balance."), HttpStatus.BAD_REQUEST);
    } else {

      Double updatedBalance = balance - withdrawal;
      account.setBalance(updatedBalance);
      Boolean isUpdated = accountService.updateAccountBalance(account);

      if (isUpdated) {

        AccountActivity accountActivity = new AccountActivity(account,
            ActivityType.WITHDRAWAL, withdrawal, "Money Withdrawn " +
            "Successfully");
        accountActivityRepository.save(accountActivity);
        return new ResponseEntity<>(new ApiResponse(true, "Money withdrawn" + " successfully."), HttpStatus.OK);
      } else {
        return new ResponseEntity<>(new ApiResponse(false, "Money withdrawal " + "not successful."), HttpStatus.BAD_REQUEST);
      }
    }
  }

}
