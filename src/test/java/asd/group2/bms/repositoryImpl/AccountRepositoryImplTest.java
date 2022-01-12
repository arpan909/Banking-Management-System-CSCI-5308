package asd.group2.bms.repositoryImpl;

import asd.group2.bms.model.account.Account;
import asd.group2.bms.model.account.AccountType;
import asd.group2.bms.model.user.User;
import asd.group2.bms.repositoryMapper.AccountRowMapper;
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

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AccountRepositoryImplTest {

  @Mock
  private JdbcTemplate jdbcTemplate;

  @InjectMocks
  private AccountRepositoryImpl accountRepository;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.initMocks(this);
    ReflectionTestUtils.setField(accountRepository, "jdbcTemplate", jdbcTemplate);
  }

  @Test
  void findAccountByUser_IdSuccessTest() {
    Long userId = 1L;

    User user = new User();
    user.setId(userId);

    Account account = new Account();
    account.setUser(user);

    when(jdbcTemplate.queryForObject(
        ArgumentMatchers.anyString(),
        ArgumentMatchers.any(),
        ArgumentMatchers.any(AccountRowMapper.class)))
        .thenReturn(account);

    Optional<Account> accountOptional =
        accountRepository.findAccountByUser_Id(userId);

    assertEquals(userId, accountOptional.get().getUser().getId());
  }

  @Test
  void findAccountByUser_IdNotFoundTest() {
    Long userId = 1L;

    when(jdbcTemplate.queryForObject(
        ArgumentMatchers.anyString(),
        ArgumentMatchers.any(),
        ArgumentMatchers.any(AccountRowMapper.class)))
        .thenThrow(new EmptyResultDataAccessException(0));

    Optional<Account> accountOptional =
        accountRepository.findAccountByUser_Id(userId);

    assertEquals(Optional.empty(), accountOptional);
  }

  @Test
  void findAccountByAccountNumberSuccessTest() {
    Long accountNumber = 1L;

    Account account = new Account();
    account.setAccountNumber(accountNumber);

    when(jdbcTemplate.queryForObject(
        ArgumentMatchers.anyString(),
        ArgumentMatchers.any(),
        ArgumentMatchers.any(AccountRowMapper.class)))
        .thenReturn(account);

    Optional<Account> accountOptional =
        accountRepository.findAccountByAccountNumber(accountNumber);

    assertEquals(accountNumber, accountOptional.get().getAccountNumber());
  }

  @Test
  void findAccountByAccountNumberNotFoundTest() {
    Long accountNumber = 1L;

    when(jdbcTemplate.queryForObject(
        ArgumentMatchers.anyString(),
        ArgumentMatchers.any(),
        ArgumentMatchers.any(AccountRowMapper.class)))
        .thenThrow(new EmptyResultDataAccessException(0));

    Optional<Account> accountOptional =
        accountRepository.findAccountByAccountNumber(accountNumber);

    assertEquals(Optional.empty(), accountOptional);
  }

  @Test
  void saveTest() {
    Long userId = 1L;

    User user = new User();
    user.setId(userId);

    Account account = new Account();
    account.setAccountType(AccountType.SAVINGS);
    account.setUser(user);

    when(jdbcTemplate.update(ArgumentMatchers.anyString(),
        (Object[]) ArgumentMatchers.any())).thenReturn(5);

    accountRepository.save(account);
    assertEquals(1L, account.getUser().getId());
  }

  @Test
  void updateFalseTest() {
    Account account = new Account();
    account.setAccountNumber(1L);

    when(jdbcTemplate.update(ArgumentMatchers.anyString(),
        (Object[]) ArgumentMatchers.any())).thenReturn(0);

    assertFalse(accountRepository.update(account));
  }

  @Test
  void updateTrueTest() {
    Account account = new Account();
    account.setAccountNumber(1L);

    when(jdbcTemplate.update(ArgumentMatchers.anyString(),
        (Object[]) ArgumentMatchers.any())).thenReturn(5);

    assertTrue(accountRepository.update(account));
  }

}