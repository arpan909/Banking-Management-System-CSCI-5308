package asd.group2.bms.serviceImpl;

import asd.group2.bms.model.account.Account;
import asd.group2.bms.model.account.AccountActivity;
import asd.group2.bms.model.term_deposit.TermDepositDetail;
import asd.group2.bms.model.term_deposit.TermDepositStatus;
import asd.group2.bms.model.user.User;
import asd.group2.bms.repository.IAccountActivityRepository;
import asd.group2.bms.repository.ITermDepositDetailRepository;
import asd.group2.bms.service.IAccountService;
import asd.group2.bms.service.ICustomEmail;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TermDepositDetailServiceImplTest {

  @Mock
  ITermDepositDetailRepository termDepositDetailRepository;

  @Mock
  IAccountActivityRepository accountActivityRepository;

  @Mock
  IAccountService accountService;

  @Mock
  ICustomEmail customEmail;

  @InjectMocks
  TermDepositDetailServiceImpl termDepositDetailService;

  @BeforeEach
  public void setup() {
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
  void makeTermDepositRequestSuccessTest() throws Exception {
    Long userId = 1L;
    String email = "arpan@gmail.com";
    String firstName = "arpan";
    Double fdAmount = 1500.0;
    Double balance = 5000.0;
    Date date = new Date();
    int duration = 4;

    User user = new User();
    user.setId(userId);
    user.setFirstName(firstName);
    user.setEmail(email);

    Account account = new Account();
    account.setUser(user);
    account.setBalance(balance);

    when(accountService.getAccountByUserId(userId)).thenReturn(account);
    when(accountService.updateAccountBalance(account)).thenReturn(true);
    when(termDepositDetailRepository.save(any(TermDepositDetail.class))).then(returnsFirstArg());
    when(accountActivityRepository.save(any(AccountActivity.class))).then(returnsFirstArg());


    ResponseEntity<?> responseEntity = termDepositDetailService.makeTermDepositRequest(userId, email, firstName,
        fdAmount, date, duration);

    assertEquals(HttpStatus.OK.toString(), responseEntity.getStatusCode().toString(),
        "Make Term Deposit success test was not executed properly");
  }

  @Test
  void makeTermDepositRequestBalanceNotUpdatedFailTest() throws Exception {
    Long userId = 1L;
    String email = "arpan@gmail.com";
    String firstName = "arpan";
    Double fdAmount = 1500.0;
    Double balance = 5000.0;
    Date date = new Date();
    int duration = 4;

    User user = new User();
    user.setId(userId);
    user.setFirstName(firstName);
    user.setEmail(email);

    Account account = new Account();
    account.setUser(user);
    account.setBalance(balance);

    when(accountService.getAccountByUserId(userId)).thenReturn(account);
    when(accountService.updateAccountBalance(account)).thenReturn(false);

    ResponseEntity<?> responseEntity = termDepositDetailService.makeTermDepositRequest(userId, email, firstName,
        fdAmount, date, duration);

    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.toString(), responseEntity.getStatusCode().toString(),
        "Make Term Deposit failure test was not executed properly because " + "balance was not updated!");
  }

  @Test
  void makeTermDepositRequestLowFDAmountTest() throws Exception {
    Long userId = 1L;
    String email = "arpan@gmail.com";
    String firstName = "arpan";
    Double fdAmount = 10.0;
    Date date = new Date();
    int duration = 4;

    ResponseEntity<?> responseEntity = termDepositDetailService.makeTermDepositRequest(userId, email, firstName,
        fdAmount, date, duration);

    assertEquals(HttpStatus.BAD_REQUEST.toString(), responseEntity.getStatusCode().toString(),
        "Make Term Deposit failure test was not executed properly because FD " + "amount was low");
  }

  @Test
  void makeTermDepositRequestNotEnoughBalanceTest() throws Exception {
    Long userId = 1L;
    String email = "arpan@gmail.com";
    String firstName = "arpan";
    Double fdAmount = 6000.0;
    Double balance = 5000.0;
    Date date = new Date();
    int duration = 4;

    User user = new User();
    user.setId(userId);
    user.setFirstName(firstName);
    user.setEmail(email);

    Account account = new Account();
    account.setUser(user);
    account.setBalance(balance);

    when(accountService.getAccountByUserId(userId)).thenReturn(account);

    ResponseEntity<?> responseEntity = termDepositDetailService.makeTermDepositRequest(userId, email, firstName,
        fdAmount, date, duration);

    assertEquals(HttpStatus.BAD_REQUEST.toString(), responseEntity.getStatusCode().toString(),
        "Make Term Deposit failure test was not executed properly because FD "
            + "amount was higher than available balance");
  }

  @Test
  void makeTermDepositRequestNotEnoughBalanceAfterDeductionTest() throws Exception {
    Long userId = 1L;
    String email = "arpan@gmail.com";
    String firstName = "arpan";
    Double fdAmount = 5000.0;
    Double balance = 5000.0;
    Date date = new Date();
    int duration = 4;

    User user = new User();
    user.setId(userId);
    user.setFirstName(firstName);
    user.setEmail(email);

    Account account = new Account();
    account.setUser(user);
    account.setBalance(balance);

    when(accountService.getAccountByUserId(userId)).thenReturn(account);

    ResponseEntity<?> responseEntity = termDepositDetailService.makeTermDepositRequest(userId, email, firstName,
        fdAmount, date, duration);
    assertEquals(HttpStatus.BAD_REQUEST.toString(), responseEntity.getStatusCode().toString(),
        "Make Term Deposit failure test was not executed properly because " + "balance after deduction is lower");
  }

  @Test
  void makeTermDepositRequestExceptionTest() throws Exception {
    Long userId = 1L;
    String email = "arpan@gmail.com";
    String firstName = "arpan";
    Double fdAmount = 4900.0;
    Double balance = 5000.0;
    Date date = new Date();
    int duration = 4;

    User user = new User();
    user.setId(userId);
    user.setFirstName(firstName);
    user.setEmail(email);

    Account account = new Account();
    account.setBalance(balance);

    when(accountService.getAccountByUserId(userId)).thenThrow(new RuntimeException());

    ResponseEntity<?> responseEntity = termDepositDetailService.makeTermDepositRequest(userId, email, firstName,
        fdAmount, date, duration);
    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,
        responseEntity.getStatusCode(),
        "Make Term Deposit failure test was not executed properly because " +
            "exception was raised");
  }

  @Test
  void getTermDepositDetailByIdTest() {
    Long id = 1L;
    Double initialAmount = 1500.0;
    TermDepositDetail termDepositDetail = new TermDepositDetail();
    termDepositDetail.setInitialAmount(initialAmount);

    when(termDepositDetailRepository.findById(id)).thenReturn(Optional.of(termDepositDetail));

    TermDepositDetail fetchedTermDeposit = termDepositDetailService.getTermDepositDetailById(id);
    assertEquals(initialAmount, fetchedTermDeposit.getInitialAmount(), "Wrong " + "Initial Amount was returned");
  }

  @Test
  void getTermDepositDetailTest() {
    Long userId = 1L;
    Long accountNumber = 1L;
    String email = "arpan@gmail.com";
    String firstName = "arpan";
    Double balance = 5000.0;

    List<TermDepositDetail> termDepositDetails = new ArrayList<>();

    User user = new User();
    user.setId(userId);
    user.setFirstName(firstName);
    user.setEmail(email);

    Account account = new Account();
    account.setAccountNumber(accountNumber);
    account.setUser(user);
    account.setBalance(balance);

    TermDepositDetail termDepositDetail = new TermDepositDetail();
    termDepositDetail.setAccount(account);
    termDepositDetails.add(termDepositDetail);

    when(termDepositDetailRepository.findTermDepositDetailByAccount_AccountNumber(accountNumber))
        .thenReturn(termDepositDetails);
    when(accountService.getAccountByUserId(userId)).thenReturn(account);

    assertEquals(accountNumber,
        termDepositDetailService.getTermDepositDetail(userId).get(0).getAccount().getAccountNumber(),
        "Wrong accountNumber was returned");
  }

  @Test
  void getTermDepositDetailEmptyTest() {
    Long userId = 1L;
    Long accountNumber = 1L;
    String email = "arpan@gmail.com";
    String firstName = "arpan";
    Double balance = 5000.0;

    List<TermDepositDetail> termDepositDetails = new ArrayList<>();

    User user = new User();
    user.setId(userId);
    user.setFirstName(firstName);
    user.setEmail(email);

    Account account = new Account();
    account.setAccountNumber(accountNumber);
    account.setUser(user);
    account.setBalance(balance);

    when(accountService.getAccountByUserId(userId)).thenReturn(account);
    when(termDepositDetailRepository.findTermDepositDetailByAccount_AccountNumber(accountNumber))
        .thenReturn(termDepositDetails);

    assertEquals(0, termDepositDetailService.getTermDepositDetail(userId).size(),
        "Empty " + "Term Deposit list was not returned");
  }

  @Test
  void closeTermDepositeDetailTrueTest() {
    Long accountNumber = 1L;
    Double balance = 100000.0;
    Double initialAmount = 5000.0;

    Date fromDate = Date.from(LocalDate.of(2021, 7, 1).atStartOfDay(ZoneId.systemDefault()).toInstant());
    Date toDate = Date.from(LocalDate.of(2021, 9, 1).atStartOfDay(ZoneId.systemDefault()).toInstant());

    Account account = new Account();
    account.setAccountNumber(accountNumber);
    account.setBalance(balance);

    TermDepositDetail termDepositDetail = new TermDepositDetail();
    termDepositDetail.setAccount(account);
    termDepositDetail.setStartDate(fromDate);
    termDepositDetail.setMaturityDate(toDate);
    termDepositDetail.setInitialAmount(initialAmount);
    termDepositDetail.setTermDepositStatus(TermDepositStatus.ACTIVE);

    when(accountService.updateAccountBalance(account)).thenReturn(true);
    when(termDepositDetailRepository.update(termDepositDetail)).thenReturn(true);

    assertEquals(true, termDepositDetailService.closeTermDepositDetail(termDepositDetail), "Term deposit not closed!");
  }

  @Test
  void closeTermDepositeDetailFalseTest() {

    Long accountNumber = 1L;
    Double balance = 100000.0;
    Double initialAmount = 5000.0;

    Date fromDate = Date.from(LocalDate.of(2021, 7, 1).atStartOfDay(ZoneId.systemDefault()).toInstant());
    Date toDate = Date.from(LocalDate.of(2021, 9, 1).atStartOfDay(ZoneId.systemDefault()).toInstant());

    Account account = new Account();
    account.setAccountNumber(accountNumber);
    account.setBalance(balance);

    TermDepositDetail termDepositDetail = new TermDepositDetail();
    termDepositDetail.setAccount(account);
    termDepositDetail.setStartDate(fromDate);
    termDepositDetail.setMaturityDate(toDate);
    termDepositDetail.setInitialAmount(initialAmount);
    termDepositDetail.setTermDepositStatus(TermDepositStatus.MATURED);

    when(accountService.updateAccountBalance(account)).thenReturn(true);
    when(termDepositDetailRepository.update(termDepositDetail)).thenReturn(true);

    assertEquals(true, termDepositDetailService.closeTermDepositDetail(termDepositDetail),
        "Term deposit cannot be closed!");
  }

  @Test
  void checkActiveTermDepositTrueTest() {
    List<TermDepositDetail> termDepositDetailList = new ArrayList<>();

    TermDepositDetail termDepositDetail = new TermDepositDetail();
    termDepositDetail.setTermDepositStatus(TermDepositStatus.ACTIVE);
    termDepositDetailList.add(termDepositDetail);

    assertEquals(true, termDepositDetailService.checkActiveTermDeposit(termDepositDetailList),
        "Term deposit status was not ACTIVE");
  }

  @Test
  void checkActiveTermDepositFalseTest() {
    List<TermDepositDetail> termDepositDetailList = new ArrayList<>();

    TermDepositDetail termDepositDetail = new TermDepositDetail();
    termDepositDetail.setTermDepositStatus(TermDepositStatus.CLOSED);
    termDepositDetailList.add(termDepositDetail);

    assertEquals(false, termDepositDetailService.checkActiveTermDeposit(termDepositDetailList),
        "Term deposit status was not CLOSED");
  }

}
