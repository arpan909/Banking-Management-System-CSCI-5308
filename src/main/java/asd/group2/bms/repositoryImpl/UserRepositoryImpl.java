package asd.group2.bms.repositoryImpl;

import asd.group2.bms.exception.ResourceNotFoundException;
import asd.group2.bms.model.user.AccountStatus;
import asd.group2.bms.model.user.Role;
import asd.group2.bms.model.user.User;
import asd.group2.bms.repository.IUserRepository;
import asd.group2.bms.repositoryMapper.UserRowMapper;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public class UserRepositoryImpl extends JdbcDaoSupport implements IUserRepository {

  private final JdbcTemplate jdbcTemplate;

  final
  DataSource dataSource;

  final
  RoleRepositoryImpl roleRepository;

  public UserRepositoryImpl(JdbcTemplate jdbcTemplate, DataSource dataSource, RoleRepositoryImpl roleRepository) {
    this.jdbcTemplate = jdbcTemplate;
    this.dataSource = dataSource;
    this.roleRepository = roleRepository;
  }

  @PostConstruct
  private void initialize() {
    setDataSource(dataSource);
  }

  public List<User> findByIdIn(List<Long> userIds) {
    String inSql = String.join(",", Collections.nCopies(userIds.size(), "?"));
    String sql = String.format("SELECT * FROM users u INNER JOIN user_roles ur ON u.id = ur.user_id INNER JOIN roles r ON r.id = ur.role_id WHERE id IN (%s)", inSql);
    return jdbcTemplate.query(sql, userIds.toArray(),
        new UserRowMapper());
  }

  public Boolean existsByEmail(String email) {
    String sql = "SELECT count(*) FROM users WHERE email = ?";

    int count = jdbcTemplate.queryForObject(sql, new Object[]{email}, Integer.class);

    return count > 0;
  }

  public Boolean existsByUsername(String username) {
    String sql = "SELECT count(*) FROM users WHERE username = ?";

    int count = jdbcTemplate.queryForObject(sql, new Object[]{username},
        Integer.class);

    return count > 0;
  }

  public Optional<User> findByUsernameOrEmail(String username, String email) {
    String sql = "SELECT * FROM users u INNER JOIN user_roles ur ON u.id = " +
        "ur.user_id INNER JOIN roles r ON r.id = ur.role_id WHERE username = " +
        "? OR email = ?";
    try {
      return Optional.ofNullable(jdbcTemplate.queryForObject(sql,
          new Object[]{username, email},
          new UserRowMapper()));
    } catch (EmptyResultDataAccessException e) {
      return Optional.empty();
    }
  }

  public Optional<User> findById(Long userId) {
    String sql = "SELECT * FROM users u INNER JOIN user_roles ur ON u.id = " +
        "ur.user_id INNER JOIN roles r ON r.id = ur.role_id WHERE u.id = ?";
    try {
      return Optional.ofNullable(jdbcTemplate.queryForObject(sql, new Object[]{userId},
          new UserRowMapper()));
    } catch (EmptyResultDataAccessException e) {
      return Optional.empty();
    }
  }

  public Page<User> findByAccountStatusEquals(AccountStatus accountStatus, Pageable pageable) {
    String rowCountSql = "SELECT count(1) AS row_count " +
        "FROM users " +
        "WHERE account_status = ?";

    int total =
        jdbcTemplate.queryForObject(
            rowCountSql,
            new Object[]{accountStatus.ordinal()},
            (rs, rowNum) -> rs.getInt(1)
        );

    String querySql = "SELECT * FROM users u INNER JOIN user_roles ur ON u.id = " +
        "ur.user_id INNER JOIN roles r ON r.id = ur.role_id WHERE account_status = ? " +
        "LIMIT " + pageable.getPageSize() +
        " OFFSET " + pageable.getOffset();

    List<User> users = jdbcTemplate.query(
        querySql,
        new Object[]{accountStatus.ordinal()}, new UserRowMapper()
    );

    return new PageImpl<>(users, pageable, total);
  }

  public Optional<User> findByEmail(String email) {
    String sql = "SELECT * FROM users u INNER JOIN user_roles ur ON u.id = ur.user_id INNER JOIN roles r ON r.id = ur.role_id WHERE email = ?";
    try {
      return Optional.ofNullable(jdbcTemplate.queryForObject(sql, new Object[]{email},
          new UserRowMapper()));
    } catch (EmptyResultDataAccessException e) {
      return Optional.empty();
    }
  }

  public Optional<User> findByForgotPasswordToken(String forgotPasswordToken) {
    String sql = "SELECT * FROM users u INNER JOIN user_roles ur ON u.id = ur.user_id INNER JOIN roles r ON r.id = ur.role_id WHERE forgot_password_token = ?";
    try {
      return Optional.ofNullable(jdbcTemplate.queryForObject(sql,
          new Object[]{forgotPasswordToken},
          new UserRowMapper()));
    } catch (EmptyResultDataAccessException e) {
      return Optional.empty();
    }
  }

  public Optional<User> findByUsername(String username) {
    String sql = "SELECT * FROM users u INNER JOIN user_roles ur ON u.id = ur.user_id INNER JOIN roles r ON r.id = ur.role_id WHERE username = ?";
    try {
      return Optional.ofNullable(jdbcTemplate.queryForObject(sql,
          new Object[]{username},
          new UserRowMapper()));
    } catch (EmptyResultDataAccessException e) {
      return Optional.empty();
    }
  }

  public User save(User user) {

    Date now = new Date();
    KeyHolder keyHolder = new GeneratedKeyHolder();

    String userSql = "INSERT INTO users (" +
        "created_at, updated_at, account_status, address, " +
        "birthday, email, first_name, last_name, " +
        "password, phone, username, forgot_password_token, " +
        "requested_account_type, city, state, zip_code" +
        ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";


    jdbcTemplate.update(
        connection -> {
          PreparedStatement ps =
              connection.prepareStatement(userSql,
                  Statement.RETURN_GENERATED_KEYS);
          ps.setDate(1, new java.sql.Date(now.getTime()));
          ps.setDate(2, new java.sql.Date(now.getTime()));
          ps.setInt(3, AccountStatus.valueOf(user.getAccountStatus().toString()).ordinal());
          ps.setString(4, user.getAddress());
          ps.setDate(5, new java.sql.Date(user.getBirthday().getTime()));
          ps.setString(6, user.getEmail());
          ps.setString(7, user.getFirstName());
          ps.setString(8, user.getLastName());
          ps.setString(9, user.getPassword());
          ps.setString(10, user.getPhone());
          ps.setString(11, user.getUsername());
          ps.setString(12, user.getForgotPasswordToken());
          ps.setString(13, user.getRequestedAccountType().name());
          ps.setString(14, user.getCity());
          ps.setString(15, user.getState());
          ps.setString(16, user.getZipCode());
          return ps;
        }, keyHolder);

    Role role =
        roleRepository.findByName(user.getRoles().iterator().next().getName()).orElseThrow(() -> new ResourceNotFoundException("Role", "role", "Role not found"));

    String roleSql = "INSERT INTO user_roles (user_id, role_id) VALUES (?, ?)";
    jdbcTemplate.update(roleSql, keyHolder.getKey().longValue(),
        role.getId());

    return user;
  }

  public Boolean update(User user) {
    String sql = "UPDATE users SET " +
        "updated_at = ?, account_status = ?, address = ?, " +
        "birthday = ?, first_name = ?, last_name = ?, " +
        "password = ?, phone = ?, forgot_password_token = ?, " +
        "requested_account_type = ?, city = ?, state = ?, zip_code = ?" +
        " WHERE id = ?";
    int status = jdbcTemplate.update(sql,
        new Date(),
        AccountStatus.valueOf(user.getAccountStatus().toString()).ordinal(),
        user.getAddress(),
        user.getBirthday(), user.getFirstName(), user.getLastName(),
        user.getPassword(), user.getPhone(),
        user.getForgotPasswordToken(),
        user.getRequestedAccountType().name(),
        user.getCity(), user.getState(), user.getZipCode(), user.getId());

    if (status == 0) {
      return false;
    }

    return true;
  }

}
