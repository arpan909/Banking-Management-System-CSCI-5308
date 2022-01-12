package asd.group2.bms.serviceImpl;

import asd.group2.bms.model.account.Account;
import asd.group2.bms.model.cards.credit.CreditCard;
import asd.group2.bms.model.cards.credit.CreditCardStatus;
import asd.group2.bms.model.user.User;
import asd.group2.bms.payload.response.CreditCardListResponse;
import asd.group2.bms.payload.response.PagedResponse;
import asd.group2.bms.repository.ICreditCardRepository;
import asd.group2.bms.service.ICustomEmail;
import asd.group2.bms.util.CardDetails;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CreditCardServiceImplTest {

  @Mock
  ICreditCardRepository creditCardRepository;

  @Mock
  ICustomEmail customEmail;

  @Mock
  Helper helper;

  @InjectMocks
  CreditCardServiceImpl creditCardService;

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
  void getCreditCardListByStatusTestPass() {
    int page = 0;
    int size = 3;
    Pageable pageable = PageRequest.of(page, size, Sort.Direction.ASC, "createdAt");
    Long creditCardNumber = 123L;
    CreditCardStatus creditCardStatus = CreditCardStatus.PENDING;

    User user = new User();
    user.setUsername("aditya");
    Account account = new Account();
    account.setUser(user);
    CreditCard creditCard = new CreditCard();
    creditCard.setCreditCardNumber(123L);
    creditCard.setAccount(account);
    List<CreditCard> cards = new ArrayList<>();
    cards.add(creditCard);
    Page<CreditCard> pagedCards = new PageImpl<>(cards);

    when(creditCardRepository.findByCreditCardStatusEquals(creditCardStatus,
        pageable)).thenReturn(pagedCards);

    PagedResponse<CreditCardListResponse> creditCards =
        creditCardService.getCreditCardListByStatus(creditCardStatus, page,
            size);

    assertEquals(creditCardNumber, creditCards.getContent().get(0).getCreditCardNumber());
  }

  @Test
  void getCreditCardListByStatusTestEmpty() {
    int page = 0;
    int size = 3;
    Pageable pageable = PageRequest.of(page, size, Sort.Direction.ASC, "createdAt");
    CreditCardStatus creditCardStatus = CreditCardStatus.PENDING;

    List<CreditCard> cards = new ArrayList<>();
    Page<CreditCard> pagedCards = new PageImpl<>(cards);

    when(creditCardRepository.findByCreditCardStatusEquals(creditCardStatus,
        pageable)).thenReturn(pagedCards);

    PagedResponse<CreditCardListResponse> creditCards =
        creditCardService.getCreditCardListByStatus(creditCardStatus, page,
            size);

    assertEquals(0, creditCards.getSize());
  }

  @Test
  void getCreditCardByCreditCardNumberTest() {
    Long creditCardNumber = 123L;
    when(creditCardRepository.findById(creditCardNumber)).thenReturn(Optional.of(new CreditCard()));

    creditCardService.getCreditCardByCreditCardNumber(creditCardNumber);
    verify(creditCardRepository, times(1)).findById(any());
  }

  @Test
  void setCreditCardRequestStatusTestPassCondition1() throws MessagingException,
      UnsupportedEncodingException {
    CreditCard creditCard = new CreditCard();
    Long card = 123L;
    User user = new User();
    user.setEmail("abc");
    user.setFirstName("aditya");
    Account account = new Account();
    account.setUser(user);
    account.setCreditScore(660);
    creditCard.setAccount(account);
    CreditCardStatus creditCardStatus = CreditCardStatus.APPROVED;
    creditCard.setCreditCardNumber(card);
    creditCard.setTransactionLimit(1500);

    when(creditCardRepository.findById(any())).thenReturn(Optional.of(creditCard));
    when(creditCardRepository.update(any())).thenReturn(true);
    doNothing().when(customEmail).sendCreditCardApprovalMail(any(), any(),
        any());

    creditCardService.setCreditCardRequestStatus(card, creditCardStatus);
    verify(creditCardRepository, times(1)).update(any());
  }

  @Test
  void setCreditCardRequestStatusTestPassCondition2() throws MessagingException,
      UnsupportedEncodingException {
    CreditCard creditCard = new CreditCard();
    Long card = 123L;
    User user = new User();
    user.setEmail("abc");
    user.setFirstName("aditya");
    Account account = new Account();
    account.setUser(user);
    account.setCreditScore(720);
    creditCard.setAccount(account);
    CreditCardStatus creditCardStatus = CreditCardStatus.APPROVED;
    creditCard.setCreditCardNumber(card);
    creditCard.setTransactionLimit(4000);

    when(creditCardRepository.findById(any())).thenReturn(Optional.of(creditCard));
    when(creditCardRepository.update(any())).thenReturn(true);
    doNothing().when(customEmail).sendCreditCardApprovalMail(any(), any(),
        any());

    creditCardService.setCreditCardRequestStatus(card, creditCardStatus);
    verify(creditCardRepository, times(1)).update(any());
  }

  @Test
  void setCreditCardRequestStatusTestPassCondition3() throws MessagingException,
      UnsupportedEncodingException {
    CreditCard creditCard = new CreditCard();
    Long card = 123L;
    User user = new User();
    user.setEmail("abc");
    user.setFirstName("aditya");
    Account account = new Account();
    account.setUser(user);
    account.setCreditScore(760);
    creditCard.setAccount(account);
    CreditCardStatus creditCardStatus = CreditCardStatus.APPROVED;
    creditCard.setCreditCardNumber(card);
    creditCard.setTransactionLimit(4000);

    when(creditCardRepository.findById(any())).thenReturn(Optional.of(creditCard));
    when(creditCardRepository.update(any())).thenReturn(true);
    doNothing().when(customEmail).sendCreditCardApprovalMail(any(), any(),
        any());

    creditCardService.setCreditCardRequestStatus(card, creditCardStatus);
    verify(creditCardRepository, times(1)).update(any());
  }

  @Test
  void setCreditCardRequestStatusTestPassCondition4() throws MessagingException,
      UnsupportedEncodingException {
    CreditCard creditCard = new CreditCard();
    Long card = 123L;
    User user = new User();
    user.setEmail("abc");
    user.setFirstName("aditya");
    Account account = new Account();
    account.setUser(user);
    account.setCreditScore(810);
    creditCard.setAccount(account);
    CreditCardStatus creditCardStatus = CreditCardStatus.APPROVED;
    creditCard.setCreditCardNumber(card);
    creditCard.setTransactionLimit(5100);

    when(creditCardRepository.findById(any())).thenReturn(Optional.of(creditCard));
    when(creditCardRepository.update(any())).thenReturn(true);
    doNothing().when(customEmail).sendCreditCardApprovalMail(any(), any(),
        any());

    creditCardService.setCreditCardRequestStatus(card, creditCardStatus);
    verify(creditCardRepository, times(1)).update(any());
  }

  @Test
  void createCreditCardTestSuccess() throws Exception {
    Account account = new Account();
    account.setAccountNumber(123L);
    account.setCreditScore(750);
    Integer requestedTransactionLimit = 1000;

    CreditCard card = new CreditCard();
    card.setCreditCardNumber(123456L);
    CardDetails cardDetails = new CardDetails();
    cardDetails.setCardNumber("123456");
    cardDetails.setExpiryMonth("12");
    cardDetails.setExpiryYear("2022");
    cardDetails.setPin("1234");
    cardDetails.setCvv("123");

    when(helper.generateCardDetails()).thenReturn(cardDetails);
    when(creditCardRepository.save(any())).thenReturn(card);

    CreditCard creditCard = creditCardService.createCreditCard(account,
        requestedTransactionLimit);

    assertEquals(123456L, creditCard.getCreditCardNumber());
  }

  @Test
  void createCreditCardTestFail() {
    Account account = new Account();
    account.setAccountNumber(123L);
    account.setCreditScore(600);
    Integer requestedTransactionLimit = 1000;

    CreditCard card = new CreditCard();
    card.setCreditCardNumber(123456L);
    CardDetails cardDetails = new CardDetails();
    cardDetails.setCardNumber("123456");
    cardDetails.setExpiryMonth("12");
    cardDetails.setExpiryYear("2022");
    cardDetails.setPin("1234");
    cardDetails.setCvv("123");

    when(helper.generateCardDetails()).thenReturn(cardDetails);

    Exception exception = assertThrows(Exception.class,
        () -> creditCardService.createCreditCard(account,
            requestedTransactionLimit));

    String expectedMessage = "You are not eligible to apply for our Credit Card";

    assertEquals(expectedMessage, exception.getMessage());
  }

  @Test
  void setCreditCardPinTestSuccess() throws Exception {
    Long card = 123L;
    String pin = "1234";
    Long id = 1L;
    User user = new User();
    user.setId(1L);
    Account account = new Account();
    account.setUser(user);
    CreditCard creditCard = new CreditCard();
    creditCard.setAccount(account);

    when(creditCardRepository.findById(any())).thenReturn(Optional.of(creditCard));
    when(creditCardRepository.update(any())).thenReturn(true);

    Boolean response = creditCardService.setCreditCardPin(card, pin, id);

    verify(creditCardRepository, times(1)).update(any());
    assertEquals(true, response);
  }

  @Test
  void setCreditCardPinTestFail() {
    Long card = 123L;
    String pin = "1234";
    Long id = 2L;
    User user = new User();
    user.setId(1L);
    Account account = new Account();
    account.setUser(user);
    CreditCard creditCard = new CreditCard();
    creditCard.setAccount(account);

    when(creditCardRepository.findById(any())).thenReturn(Optional.of(creditCard));


    Exception exception = assertThrows(Exception.class,
        () -> creditCardService.setCreditCardPin(card, pin, id));

    String expectedMessage = "You are not authorized to perform this operation";

    assertEquals(expectedMessage, exception.getMessage());

  }

}
