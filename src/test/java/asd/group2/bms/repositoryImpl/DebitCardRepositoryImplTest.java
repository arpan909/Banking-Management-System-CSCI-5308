package asd.group2.bms.repositoryImpl;

import asd.group2.bms.model.account.Account;
import asd.group2.bms.model.cards.debit.DebitCard;
import asd.group2.bms.model.cards.debit.DebitCardStatus;
import asd.group2.bms.repositoryMapper.DebitCardRowMapper;
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
class DebitCardRepositoryImplTest {

  @Mock
  private JdbcTemplate jdbcTemplate;

  @InjectMocks
  private DebitCardRepositoryImpl debitCardRepository;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.initMocks(this);
    ReflectionTestUtils.setField(debitCardRepository, "jdbcTemplate", jdbcTemplate);
  }

  @Test
  void findByIdTest() {
    Long debitCardNumber = 6L;

    DebitCard debitCard = new DebitCard();
    debitCard.setDebitCardNumber(debitCardNumber);

    when(jdbcTemplate.queryForObject(
        ArgumentMatchers.anyString(),
        ArgumentMatchers.any(),
        ArgumentMatchers.any(DebitCardRowMapper.class)))
        .thenReturn(debitCard);

    Optional<DebitCard> debitCardOptional =
        debitCardRepository.findById(debitCardNumber);

    assertEquals(debitCardNumber, debitCardOptional.get().getDebitCardNumber());
  }

  @Test
  void findByIdNotFoundTest() {
    Long debitCardNumber = 6L;

    when(Optional.ofNullable(jdbcTemplate.queryForObject(
        ArgumentMatchers.anyString(),
        ArgumentMatchers.any(),
        ArgumentMatchers.any(DebitCardRowMapper.class))))
        .thenThrow(new EmptyResultDataAccessException(0));

    Optional<DebitCard> debitCardOptional =
        debitCardRepository.findById(debitCardNumber);

    assertEquals(Optional.empty(), debitCardOptional);
  }

  @Test
  void findByAccountNumberTest() {
    Long debitCardNumber = 6L;
    Long accountNumber = 123L;

    DebitCard debitCard = new DebitCard();
    Account account = new Account();
    account.setAccountNumber(accountNumber);

    debitCard.setDebitCardNumber(debitCardNumber);
    debitCard.setAccount(account);

    when(jdbcTemplate.queryForObject(
        ArgumentMatchers.anyString(),
        ArgumentMatchers.any(),
        ArgumentMatchers.any(DebitCardRowMapper.class)))
        .thenReturn(debitCard);

    Optional<DebitCard> debitCardOptional =
        debitCardRepository.findByAccountNumber(accountNumber);

    assertEquals(debitCardNumber, debitCardOptional.get().getDebitCardNumber());

  }

  @Test
  void findByAccountNumberNotFoundTest() {
    Long debitCardNumber = 6L;
    Long accountNumber = 123L;

    when(Optional.ofNullable(jdbcTemplate.queryForObject(
        ArgumentMatchers.anyString(),
        ArgumentMatchers.any(),
        ArgumentMatchers.any(DebitCardRowMapper.class))))
        .thenThrow(new EmptyResultDataAccessException(0));

    Optional<DebitCard> debitCardOptional =
        debitCardRepository.findByAccountNumber(accountNumber);

    assertEquals(Optional.empty(), debitCardOptional);

  }

  @Test
  void save() {
    Long debitCardNumber = 6L;
    Long accountNumber = 123L;
    DebitCardStatus debitCardStatus = DebitCardStatus.ACTIVE;
    DebitCard debitCard = new DebitCard();
    Account account = new Account();
    account.setAccountNumber(accountNumber);
    debitCard.setAccount(account);
    debitCard.setDebitCardNumber(debitCardNumber);
    debitCard.setDebitCardStatus(debitCardStatus);

    when(jdbcTemplate.update(ArgumentMatchers.anyString(),
        (Object[]) ArgumentMatchers.any())).thenReturn(5);

    debitCardRepository.save(debitCard);
    assertEquals(6L, debitCard.getDebitCardNumber());

  }

  @Test
  void updateTrue() {
    Long debitCardNumber = 6L;
    DebitCardStatus debitCardStatus = DebitCardStatus.ACTIVE;
    DebitCard debitCard = new DebitCard();
    debitCard.setDebitCardNumber(debitCardNumber);
    debitCard.setDebitCardStatus(debitCardStatus);

    when(jdbcTemplate.update(ArgumentMatchers.anyString(),
        (Object[]) ArgumentMatchers.any())).thenReturn(5);

    assertTrue(debitCardRepository.update(debitCard));
  }


  @Test
  void updateFalse() {
    Long debitCardNumber = 6L;
    DebitCardStatus debitCardStatus = DebitCardStatus.ACTIVE;
    DebitCard debitCard = new DebitCard();
    debitCard.setDebitCardNumber(debitCardNumber);
    debitCard.setDebitCardStatus(debitCardStatus);

    when(jdbcTemplate.update(ArgumentMatchers.anyString(),
        (Object[]) ArgumentMatchers.any())).thenReturn(0);

    assertFalse(debitCardRepository.update(debitCard));
  }

}