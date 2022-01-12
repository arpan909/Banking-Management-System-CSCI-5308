package asd.group2.bms.serviceImpl;

import asd.group2.bms.model.cards.credit.CreditCardBill;
import asd.group2.bms.repository.ICreditCardBillRepository;
import asd.group2.bms.service.ICustomEmail;
import asd.group2.bms.util.Helper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreditCardBillServiceImplTest {

  @Mock
  ICreditCardBillRepository ICreditCardBillRepository;

  @Mock
  ICustomEmail customEmail;

  @Mock
  Helper helper;

  @InjectMocks
  CreditCardBillServiceImpl creditCardBillService;

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
  void getBillsByCreditCardNumber() {
    Long creditCardNumber = 123L;
    when(ICreditCardBillRepository.showBills(creditCardNumber)).thenReturn(Optional.of(new CreditCardBill()));
    creditCardBillService.getBills(creditCardNumber);
    verify(ICreditCardBillRepository, times(1)).showBills(any());
  }

}