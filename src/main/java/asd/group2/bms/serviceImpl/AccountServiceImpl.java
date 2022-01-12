package asd.group2.bms.serviceImpl;

import asd.group2.bms.exception.ResourceNotFoundException;
import asd.group2.bms.model.account.Account;
import asd.group2.bms.model.account.AccountType;
import asd.group2.bms.model.cards.debit.DebitCard;
import asd.group2.bms.model.user.AccountStatus;
import asd.group2.bms.model.user.User;
import asd.group2.bms.payload.response.AccountDetailResponse;
import asd.group2.bms.payload.response.PagedResponse;
import asd.group2.bms.repository.IAccountRepository;
import asd.group2.bms.repository.IUserRepository;
import asd.group2.bms.security.UserPrincipal;
import asd.group2.bms.service.IAccountService;
import asd.group2.bms.service.ICustomEmail;
import asd.group2.bms.service.IDebitCardService;
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
public class AccountServiceImpl implements IAccountService {

  @Autowired
  IUserRepository userRepository;

  @Autowired
  IAccountRepository accountRepository;

  @Autowired
  IDebitCardService debitCardService;

  @Autowired
  ICustomEmail customEmail;

  @Autowired
  Helper helper;

  /**
   * @param accountStatus: Account Status (PENDING, APPROVED, REJECTED)
   * @param page:          Page Number
   * @param size:          Size of the response data
   * @return This will return all the user having status accountStatus
   */
  @Override
  public PagedResponse<User> getUserAccountListByStatus(AccountStatus accountStatus, int page, int size) {

    // Making list in ascending order to get early applied application first
    Pageable pageable = PageRequest.of(page, size, Sort.Direction.ASC, "createdAt");
    Page<User> users = userRepository.findByAccountStatusEquals(accountStatus, pageable);

    if (users.getNumberOfElements() == 0) {
      return new PagedResponse<>(Collections.emptyList(), users.getNumber(),
          users.getSize(), users.getTotalElements(), users.getTotalPages(), users.isLast());
    }

    List<User> userResponse = users.getContent();

    return new PagedResponse<>(userResponse, users.getNumber(),
        users.getSize(), users.getTotalElements(), users.getTotalPages(), users.isLast());
  }


  /**
   * @param user:        User whose account is being created
   * @param accountType: Type of account (SAVINGS, CURRENT)
   * @param balance:     balance deposited while account approval
   * @param creditScore: credit score of customer
   * @return This will return all the user having status accountStatus
   * @throws MessagingException:           This will throw MessagingException
   * @throws UnsupportedEncodingException: This will throw UnsupportedEncodingException
   */
  @Override
  public Account createAccount(User user, AccountType accountType, Double balance, int creditScore) throws MessagingException, UnsupportedEncodingException {
    String accountNumber = helper.generateRandomDigits(10);
    Account account = new Account(Long.parseLong(accountNumber), accountType, balance,
        creditScore);
    account.setUser(user);

    Account createdAccount = accountRepository.save(account);

    DebitCard debitCard = debitCardService.createDebitCard(createdAccount);
    String email = user.getEmail();
    String firstName = user.getFirstName();
    Long debitCardNumber = debitCard.getDebitCardNumber();
    String pin = debitCard.getPin();
    String expiryYear = debitCard.getExpiryYear();
    String expiryMonth = debitCard.getExpiryMonth();
    String cvv = debitCard.getCvv();

    customEmail.sendDebitCardGenerationMail(email, firstName, debitCardNumber, pin, expiryMonth, expiryYear, cvv);

    return createdAccount;
  }

  /**
   * @param userId: User id of user whose account detail is requested
   * @return This will return the account details of the user based on user id
   */
  @Override
  public Account getAccountByUserId(Long userId) {
    return accountRepository.findAccountByUser_Id(userId).orElseThrow(() -> new ResourceNotFoundException("Account", "account", "this user"));
  }

  /**
   * @param accountNumber: account number of user whose account detail is
   *                       requested
   * @return This will return the account details of the user based on
   * account number
   */
  @Override
  public Account getAccountByAccountNumber(Long accountNumber) {
    return accountRepository.findAccountByAccountNumber(accountNumber).orElseThrow(() -> new ResourceNotFoundException("Account", "account", accountNumber));
  }

  /**
   * @param account: Account model object
   * @return True of false based on updated or not
   */
  @Override
  public Boolean updateAccountBalance(Account account) {

    return accountRepository.update(account);
  }

  @Override
  public AccountDetailResponse getAccountDetails(UserPrincipal currentUser) {
    Account account = getAccountByUserId(currentUser.getId());
    AccountDetailResponse accountDetailResponse =
        ModelMapper.mapAccountToAccountDetailResponse(account);
    DebitCard debitCard =
        debitCardService.getDebitCardByAccountNumber(account.getAccountNumber());
    accountDetailResponse.setDebitCardNumber(debitCard.getDebitCardNumber());

    return accountDetailResponse;
  }


}
