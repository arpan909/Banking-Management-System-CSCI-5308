package asd.group2.bms.serviceImpl;

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
import asd.group2.bms.service.ICustomEmail;
import asd.group2.bms.service.IDebitCardService;
import asd.group2.bms.util.Helper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AccountServiceImplTest {

  @Mock
  IUserRepository userRepository;

  @Mock
  IAccountRepository accountRepository;

  @Mock
  ICustomEmail customEmail;

  @Mock
  Helper helper;

  @Mock
  IDebitCardService debitCardService;

  @InjectMocks
  AccountServiceImpl accountService;

  @BeforeEach
  void setUp() {
    HttpServletRequest mockRequest = new MockHttpServletRequest();
    ServletRequestAttributes servletRequestAttributes = new ServletRequestAttributes(mockRequest);
    RequestContextHolder.setRequestAttributes(servletRequestAttributes);
    MockitoAnnotations.openMocks(this);
  }

  @AfterEach
  public void teardown() {
    RequestContextHolder.resetRequestAttributes();
  }

  @Test
  void getUserAccountListByStatusTest() {
    String username = "harsh";
    AccountStatus accountStatus = AccountStatus.PENDING;
    int page = 0;
    int size = 3;

    Pageable pageable = PageRequest.of(page, size, Sort.Direction.ASC, "createdAt");

    User userOne = new User();
    userOne.setUsername("harsh");

    List<User> users = new ArrayList<>();
    users.add(userOne);

    Page<User> pagedUsers = new PageImpl<>(users);

    when(userRepository.findByAccountStatusEquals(accountStatus, pageable)).thenReturn(pagedUsers);

    PagedResponse<User> accounts =
        accountService.getUserAccountListByStatus(accountStatus,
            page, size);

    assertEquals(username, accounts.getContent().get(0).getUsername(), "Wrong " +
        "user was returned");
  }

  @Test
  void getUserAccountListByStatusWithEmptyListTest() {
    AccountStatus accountStatus = AccountStatus.PENDING;
    int page = 0;
    int size = 3;

    Pageable pageable = PageRequest.of(page, size, Sort.Direction.ASC, "createdAt");

    List<User> users = new ArrayList<>();

    Page<User> pagedUsers = new PageImpl<>(users);

    when(userRepository.findByAccountStatusEquals(accountStatus, pageable)).thenReturn(pagedUsers);

    PagedResponse<User> accounts =
        accountService.getUserAccountListByStatus(accountStatus,
            page, size);

    assertEquals(0, accounts.getContent().size(), "Wrong " +
        "list was returned");
  }

  @Test
  void createAccountTest() throws MessagingException,
      UnsupportedEncodingException {
    User user = new User();
    AccountType accountType = AccountType.SAVINGS;
    Double balance = 1000.0;
    int creditScore = 750;

    Account accountToBeCreated = new Account();
    accountToBeCreated.setCreditScore(creditScore);

    DebitCard debitCard = new DebitCard();
    debitCard.setDebitCardNumber(1L);

    when(helper.generateRandomDigits(10)).thenReturn("1234");
    when(accountRepository.save(any(Account.class))).thenReturn(accountToBeCreated);
    when(debitCardService.createDebitCard(accountToBeCreated)).thenReturn(debitCard);

    Account account = accountService.createAccount(user, accountType, balance,
        creditScore);

    assertEquals(creditScore, account.getCreditScore(), "Wrong " +
        "credit score was returned");
  }

  @Test
  void getAccountByUserIdTest() {
    Long userId = 1L;
    Double accountBalance = 5000.0;

    Account account = new Account();
    account.setBalance(accountBalance);

    when(accountRepository.findAccountByUser_Id(userId)).thenReturn(Optional.of(account));

    Account fetchedAccount = accountService.getAccountByUserId(userId);

    assertEquals(accountBalance, fetchedAccount.getBalance(), "Wrong balance " +
        "was returned");
  }

  @Test
  void getAccountByAccountNumberTest() {
    Long accountNumber = 1L;
    Double accountBalance = 5000.0;

    Account account = new Account();
    account.setBalance(accountBalance);

    when(accountRepository.findAccountByAccountNumber(accountNumber)).thenReturn(Optional.of(account));

    Account fetchedAccount = accountService.getAccountByAccountNumber(accountNumber);

    assertEquals(accountBalance, fetchedAccount.getBalance(), "Wrong balance " +
        "was returned");
  }

  @Test
  void updateAccountBalanceSuccessTest() {
    Double accountBalance = 5000.0;
    Account account = new Account();
    account.setBalance(accountBalance);

    when(accountRepository.update(any(Account.class))).thenReturn(true);

    Boolean isUpdated = accountService.updateAccountBalance(account);

    assertTrue(isUpdated, "False was returned");
  }

  @Test
  void updateAccountBalanceFailureTest() {
    Double accountBalance = 5000.0;
    Account account = new Account();
    account.setBalance(accountBalance);

    when(accountRepository.update(any(Account.class))).thenReturn(false);

    Boolean isUpdated = accountService.updateAccountBalance(account);

    assertFalse(isUpdated, "True was returned");
  }

  @Test
  void getAccountDetailsTest() {
    Long debitCardNumber = 1L;
    Long userId = 1L;
    Double accountBalance = 5000.0;
    String username = "test__harsh";
    String email = "test__harsh.bhatt@dal.ca";

    UserPrincipal currentUser = new UserPrincipal();
    currentUser.setId(userId);

    DebitCard debitCard = new DebitCard();
    debitCard.setDebitCardNumber(debitCardNumber);

    User user = new User();
    user.setId(userId);
    user.setFirstName("f_name");
    user.setLastName("l_name");
    user.setUsername(username);
    user.setEmail(email);
    user.setPhone("9876543210");

    Account account = new Account();
    account.setBalance(accountBalance);
    account.setUser(user);

    when(accountRepository.findAccountByUser_Id(userId)).thenReturn(Optional.of(account));
    when(debitCardService.getDebitCardByAccountNumber(account.getAccountNumber())).thenReturn(debitCard);

    AccountDetailResponse accountDetailResponse =
        accountService.getAccountDetails(currentUser);

    assertEquals(accountBalance, accountDetailResponse.getBalance(),
        "Wrong " +
            "account detail was returned");
  }

}