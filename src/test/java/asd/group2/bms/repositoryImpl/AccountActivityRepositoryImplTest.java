package asd.group2.bms.repositoryImpl;

import asd.group2.bms.model.account.Account;
import asd.group2.bms.model.account.AccountActivity;
import asd.group2.bms.repositoryMapper.AccountActivityRowMapper;
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
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AccountActivityRepositoryImplTest {

  @Mock
  private JdbcTemplate jdbcTemplate;

  @InjectMocks
  private AccountActivityRepositoryImpl accountActivityRepository;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.initMocks(this);
    ReflectionTestUtils.setField(accountActivityRepository, "jdbcTemplate", jdbcTemplate);
  }

  @Test
  void findAccountActivityByAccountNumberTest() {
    Long accountNumber = 1L;
    Account account = new Account();
    account.setAccountNumber(accountNumber);

    Date now = new Date();

    AccountActivity accountActivity = new AccountActivity();
    accountActivity.setActivityId(1L);

    List<AccountActivity> accountActivities = new ArrayList<>();
    accountActivities.add(accountActivity);

    when(jdbcTemplate.query(
        ArgumentMatchers.anyString(),
        (Object[]) ArgumentMatchers.any(),
        ArgumentMatchers.any(AccountActivityRowMapper.class)))
        .thenReturn(accountActivities);

    List<AccountActivity> accountActivityList =
        accountActivityRepository.findAccountActivityByAccountNumber(accountNumber, now, now);

    assertEquals(1L, accountActivityList.get(0).getActivityId());
  }

  @Test
  void findAccountActivityByAccountNumberEmptyTest() {
    Long accountNumber = 1L;

    Date now = new Date();

    when(jdbcTemplate.query(
        ArgumentMatchers.anyString(),
        (Object[]) ArgumentMatchers.any(),
        ArgumentMatchers.any(AccountActivityRowMapper.class)))
        .thenThrow(new EmptyResultDataAccessException(0));

    List<AccountActivity> accountActivityList =
        accountActivityRepository.findAccountActivityByAccountNumber(accountNumber, now, now);

    assertEquals(0, accountActivityList.size());
  }

  @Test
  void updateTrueTest() {
    AccountActivity accountActivity = new AccountActivity();
    accountActivity.setActivityId(1L);

    when(jdbcTemplate.update(ArgumentMatchers.anyString(),
        (Object[]) ArgumentMatchers.any())).thenReturn(0);

    assertFalse(accountActivityRepository.update(accountActivity));
  }

  @Test
  void updateFalseTest() {
    AccountActivity accountActivity = new AccountActivity();
    accountActivity.setActivityId(1L);

    when(jdbcTemplate.update(ArgumentMatchers.anyString(),
        (Object[]) ArgumentMatchers.any())).thenReturn(5);

    assertTrue(accountActivityRepository.update(accountActivity));
  }

}