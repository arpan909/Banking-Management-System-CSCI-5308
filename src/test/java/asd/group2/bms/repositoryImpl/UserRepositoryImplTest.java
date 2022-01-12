package asd.group2.bms.repositoryImpl;

import asd.group2.bms.model.account.AccountType;
import asd.group2.bms.model.user.AccountStatus;
import asd.group2.bms.model.user.User;
import asd.group2.bms.repositoryMapper.UserRowMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserRepositoryImplTest {

  @Mock
  private JdbcTemplate jdbcTemplate;

  @InjectMocks
  private UserRepositoryImpl userRepository;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.initMocks(this);
    ReflectionTestUtils.setField(userRepository, "jdbcTemplate", jdbcTemplate);
  }

  @Test
  void findByIdInTest() {
    Long userId = 1L;

    User user = new User();
    user.setId(userId);

    List<Long> userIds = new ArrayList<>(Arrays.asList(1L, 2L));

    List<User> users = new ArrayList<>();
    users.add(user);

    when(jdbcTemplate.query(
        ArgumentMatchers.anyString(),
        (Object[]) ArgumentMatchers.any(),
        ArgumentMatchers.any(UserRowMapper.class)))
        .thenReturn(users);

    List<User> userList =
        userRepository.findByIdIn(userIds);

    assertEquals(userId, userList.get(0).getId());

  }

  @Test
  void existsByEmailTest() {
    String email = "dummy@email.com";

    when(jdbcTemplate.queryForObject(
        ArgumentMatchers.anyString(),
        ArgumentMatchers.any(),
        eq(Integer.class)))
        .thenReturn(0);

    Boolean isExist =
        userRepository.existsByEmail(email);

    assertFalse(isExist);
  }

  @Test
  void existsByUsernameTest() {
    String username = "harsh";

    when(jdbcTemplate.queryForObject(
        ArgumentMatchers.anyString(),
        ArgumentMatchers.any(),
        eq(Integer.class)))
        .thenReturn(1);

    Boolean isExist =
        userRepository.existsByUsername(username);

    assertTrue(isExist);
  }

  @Test
  void findByUsernameOrEmailFoundTest() {
    String username = "harsh";
    String email = "dummy@email.com";
    Long userId = 1L;

    User user = new User();
    user.setId(userId);

    when(jdbcTemplate.queryForObject(
        ArgumentMatchers.anyString(),
        ArgumentMatchers.any(),
        ArgumentMatchers.any(UserRowMapper.class)))
        .thenReturn(user);

    Optional<User> userOptional =
        userRepository.findByUsernameOrEmail(username, email);

    assertEquals(userId, userOptional.get().getId());
  }


  @Test
  void findByUsernameOrEmailNotFoundTest() {
    String username = "harsh";
    String email = "dummy@email.com";

    when(Optional.ofNullable(jdbcTemplate.queryForObject(
        ArgumentMatchers.anyString(),
        ArgumentMatchers.any(),
        ArgumentMatchers.any(UserRowMapper.class))))
        .thenThrow(new EmptyResultDataAccessException(0));

    Optional<User> userOptional =
        userRepository.findByUsernameOrEmail(username, email);

    assertEquals(Optional.empty(), userOptional);
  }

  @Test
  void findByIdFoundTest() {
    Long userId = 1L;

    User user = new User();
    user.setId(userId);

    when(jdbcTemplate.queryForObject(
        ArgumentMatchers.anyString(),
        ArgumentMatchers.any(),
        ArgumentMatchers.any(UserRowMapper.class)))
        .thenReturn(user);

    Optional<User> userOptional =
        userRepository.findById(userId);

    assertEquals(userId, userOptional.get().getId());
  }

  @Test
  void findByIdNotFoundTest() {
    Long userId = 1L;

    when(Optional.ofNullable(jdbcTemplate.queryForObject(
        ArgumentMatchers.anyString(),
        ArgumentMatchers.any(),
        ArgumentMatchers.any(UserRowMapper.class))))
        .thenThrow(new EmptyResultDataAccessException(0));

    Optional<User> userOptional =
        userRepository.findById(userId);

    assertEquals(Optional.empty(), userOptional);
  }

  @Test
  void findByAccountStatusEqualsTest() {
    Long userId = 1L;
    Integer totalPage = 1;

    when(jdbcTemplate.queryForObject(
        ArgumentMatchers.anyString(),
        ArgumentMatchers.any(),
        ArgumentMatchers.any(RowMapper.class)))
        .thenAnswer((invocation) -> {
          return totalPage;
        });

    User user = new User();
    user.setId(userId);

    List<User> users = new ArrayList<>();
    users.add(user);

    when(jdbcTemplate.query(
        ArgumentMatchers.anyString(),
        (Object[]) ArgumentMatchers.any(),
        ArgumentMatchers.any(UserRowMapper.class)))
        .thenReturn(users);

    Pageable pageable = PageRequest.of(0, 1, Sort.Direction.ASC,
        "createdAt");

    Page<User> userPage =
        userRepository.findByAccountStatusEquals(AccountStatus.PENDING, pageable);

    assertEquals(totalPage, userPage.getTotalPages());
  }

  @Test
  void findByEmailFoundTest() {
    String email = "dummy@email.com";
    Long userId = 1L;

    User user = new User();
    user.setId(userId);

    when(jdbcTemplate.queryForObject(
        ArgumentMatchers.anyString(),
        ArgumentMatchers.any(),
        ArgumentMatchers.any(UserRowMapper.class)))
        .thenReturn(user);

    Optional<User> userOptional =
        userRepository.findByEmail(email);

    assertEquals(userId, userOptional.get().getId());
  }

  @Test
  void findByEmailNotFoundTest() {
    String email = "dummy@email.com";

    when(Optional.ofNullable(jdbcTemplate.queryForObject(
        ArgumentMatchers.anyString(),
        ArgumentMatchers.any(),
        ArgumentMatchers.any(UserRowMapper.class))))
        .thenThrow(new EmptyResultDataAccessException(0));

    Optional<User> userOptional =
        userRepository.findByEmail(email);

    assertEquals(Optional.empty(), userOptional);
  }

  @Test
  void findByForgotPasswordTokenFoundTest() {
    String token = "token";
    Long userId = 1L;

    User user = new User();
    user.setId(userId);

    when(jdbcTemplate.queryForObject(
        ArgumentMatchers.anyString(),
        ArgumentMatchers.any(),
        ArgumentMatchers.any(UserRowMapper.class)))
        .thenReturn(user);

    Optional<User> userOptional =
        userRepository.findByForgotPasswordToken(token);

    assertEquals(userId, userOptional.get().getId());
  }

  @Test
  void findByForgotPasswordTokenNotFoundTest() {
    String token = "token";

    when(Optional.ofNullable(jdbcTemplate.queryForObject(
        ArgumentMatchers.anyString(),
        ArgumentMatchers.any(),
        ArgumentMatchers.any(UserRowMapper.class))))
        .thenThrow(new EmptyResultDataAccessException(0));

    Optional<User> userOptional =
        userRepository.findByForgotPasswordToken(token);

    assertEquals(Optional.empty(), userOptional);
  }

  @Test
  void findByUsernameFoundTest() {
    String username = "name";
    Long userId = 1L;

    User user = new User();
    user.setId(userId);

    when(jdbcTemplate.queryForObject(
        ArgumentMatchers.anyString(),
        ArgumentMatchers.any(),
        ArgumentMatchers.any(UserRowMapper.class)))
        .thenReturn(user);

    Optional<User> userOptional =
        userRepository.findByUsername(username);

    assertEquals(userId, userOptional.get().getId());
  }

  @Test
  void findByUsernameNotFoundTest() {
    String username = "name";

    when(Optional.ofNullable(jdbcTemplate.queryForObject(
        ArgumentMatchers.anyString(),
        ArgumentMatchers.any(),
        ArgumentMatchers.any(UserRowMapper.class))))
        .thenThrow(new EmptyResultDataAccessException(0));

    Optional<User> userOptional =
        userRepository.findByUsername(username);

    assertEquals(Optional.empty(), userOptional);
  }

  @Test
  void updateTrueTest() {
    User user = new User();
    user.setId(1L);
    user.setAccountStatus(AccountStatus.PENDING);
    user.setRequestedAccountType(AccountType.SAVINGS);

    when(jdbcTemplate.update(ArgumentMatchers.anyString(),
        (Object[]) ArgumentMatchers.any())).thenReturn(0);

    assertFalse(userRepository.update(user));
  }

  @Test
  void updateFalseTest() {
    User user = new User();
    user.setId(1L);
    user.setAccountStatus(AccountStatus.PENDING);
    user.setRequestedAccountType(AccountType.SAVINGS);

    when(jdbcTemplate.update(ArgumentMatchers.anyString(),
        (Object[]) ArgumentMatchers.any())).thenReturn(5);

    assertTrue(userRepository.update(user));
  }

}