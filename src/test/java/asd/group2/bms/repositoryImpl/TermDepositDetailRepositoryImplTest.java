package asd.group2.bms.repositoryImpl;

import asd.group2.bms.model.account.Account;
import asd.group2.bms.model.term_deposit.TermDepositDetail;
import asd.group2.bms.model.term_deposit.TermDepositStatus;
import asd.group2.bms.repositoryMapper.TermDepositDetailRowMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TermDepositDetailRepositoryImplTest {

  @Mock
  private JdbcTemplate jdbcTemplate;

  @InjectMocks
  private TermDepositDetailRepositoryImpl termDepositDetailRepository;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.initMocks(this);
    ReflectionTestUtils.setField(termDepositDetailRepository, "jdbcTemplate", jdbcTemplate);
  }

  @Test
  void findTermDepositDetailByAccount_AccountNumberSuccessTest() {
    Long termDepositId = 1L;
    Long accountNumber = 1L;

    Account account = new Account();
    account.setAccountNumber(accountNumber);

    TermDepositDetail termDepositDetail = new TermDepositDetail();
    termDepositDetail.setTermDepositId(termDepositId);
    termDepositDetail.setAccount(account);

    List<TermDepositDetail> termDepositDetailList = new ArrayList<>();
    termDepositDetailList.add(termDepositDetail);

    when(jdbcTemplate.query(
        ArgumentMatchers.anyString(),
        (Object[]) ArgumentMatchers.any(),
        ArgumentMatchers.any(TermDepositDetailRowMapper.class)))
        .thenReturn(termDepositDetailList);

    assertEquals(accountNumber,
        termDepositDetailRepository.findTermDepositDetailByAccount_AccountNumber(accountNumber).get(0).getAccount().getAccountNumber(),
        "Wrong account " +
            "number was returned!");
  }

  @Test
  void findTermDepositDetailByAccount_AccountNumberFailTest() {
    Long termDepositId = 1L;
    Long accountNumber = 1L;

    Account account = new Account();
    account.setAccountNumber(accountNumber);

    TermDepositDetail termDepositDetail = new TermDepositDetail();
    termDepositDetail.setTermDepositId(termDepositId);
    termDepositDetail.setAccount(account);

    List<TermDepositDetail> termDepositDetailList = new ArrayList<>();
    termDepositDetailList.add(termDepositDetail);

    when(jdbcTemplate.query(
        ArgumentMatchers.anyString(),
        (Object[]) ArgumentMatchers.any(),
        ArgumentMatchers.any(TermDepositDetailRowMapper.class)))
        .thenThrow(new EmptyResultDataAccessException(1));

    assertEquals(0,
        termDepositDetailRepository.findTermDepositDetailByAccount_AccountNumber(accountNumber).size(),
        "Wrong term deposit " +
            "was returned!");
  }

  @Test
  void findByIdSuccessTest() {
    Long termDepositId = 1L;
    Long accountNumber = 1L;

    Account account = new Account();
    account.setAccountNumber(accountNumber);

    TermDepositDetail termDepositDetail = new TermDepositDetail();
    termDepositDetail.setTermDepositId(termDepositId);
    termDepositDetail.setAccount(account);

    when(jdbcTemplate.queryForObject(
        ArgumentMatchers.anyString(),
        (Object[]) ArgumentMatchers.any(),
        ArgumentMatchers.any(TermDepositDetailRowMapper.class)))
        .thenReturn(termDepositDetail);

    assertEquals(termDepositId,
        termDepositDetailRepository.findById(termDepositId).get().getTermDepositId(),
        "Wrong term deposit " +
            "was returned!");
  }

  @Test
  void findByIdFailTest() {
    Long termDepositId = 1L;
    Long accountNumber = 1L;

    Account account = new Account();
    account.setAccountNumber(accountNumber);

    TermDepositDetail termDepositDetail = new TermDepositDetail();
    termDepositDetail.setTermDepositId(termDepositId);
    termDepositDetail.setAccount(account);

    when(jdbcTemplate.queryForObject(
        ArgumentMatchers.anyString(),
        (Object[]) ArgumentMatchers.any(),
        ArgumentMatchers.any(TermDepositDetailRowMapper.class)))
        .thenThrow(new EmptyResultDataAccessException(1));

    Optional<TermDepositDetail> termOptional =
        termDepositDetailRepository.findById(termDepositId);
    assertEquals(Optional.empty(),
        termOptional,
        "Wrong term deposit " +
            "was returned!");
  }

  @Test
  void updateSuccessTest() {
    TermDepositDetail termDepositDetail = new TermDepositDetail();
    termDepositDetail.setTermDepositId(1L);
    termDepositDetail.setTermDepositStatus(TermDepositStatus.ACTIVE);

    when(jdbcTemplate.update(ArgumentMatchers.anyString(),
        (Object[]) ArgumentMatchers.any())).thenReturn(5);

    assertTrue(termDepositDetailRepository.update(termDepositDetail));
  }

  @Test
  void updateFailTest() {
    TermDepositDetail termDepositDetail = new TermDepositDetail();
    termDepositDetail.setTermDepositId(1L);
    termDepositDetail.setTermDepositStatus(TermDepositStatus.ACTIVE);

    when(jdbcTemplate.update(ArgumentMatchers.anyString(),
        (Object[]) ArgumentMatchers.any())).thenReturn(0);

    assertFalse(termDepositDetailRepository.update(termDepositDetail));
  }

}
