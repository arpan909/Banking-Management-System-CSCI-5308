package asd.group2.bms.repositoryImpl;

import asd.group2.bms.model.account.Account;
import asd.group2.bms.repository.IAccountRepository;
import asd.group2.bms.repositoryMapper.AccountRowMapper;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.util.Date;
import java.util.Optional;

@Repository
public class AccountRepositoryImpl extends JdbcDaoSupport implements IAccountRepository {

  private final JdbcTemplate jdbcTemplate;

  final
  DataSource dataSource;

  public AccountRepositoryImpl(JdbcTemplate jdbcTemplate, DataSource dataSource) {
    this.jdbcTemplate = jdbcTemplate;
    this.dataSource = dataSource;
  }

  @PostConstruct
  private void initialize() {
    setDataSource(dataSource);
  }

  @Override
  public Optional<Account> findAccountByUser_Id(Long userId) {
    String sql = "SELECT * FROM accounts a INNER JOIN users u ON a.user_id = u.id " +
        "INNER JOIN user_roles ur ON u.id = ur.user_id INNER JOIN roles r ON " +
        "r.id = ur.role_id WHERE a.user_id = ?";
    try {
      return Optional.ofNullable(jdbcTemplate.queryForObject(sql,
          new Object[]{userId},
          new AccountRowMapper()));
    } catch (EmptyResultDataAccessException e) {
      return Optional.empty();
    }
  }

  @Override
  public Optional<Account> findAccountByAccountNumber(Long accountNumber) {
    String sql = "SELECT * FROM accounts a INNER JOIN users u ON a.user_id = u.id " +
        "INNER JOIN user_roles ur ON u.id = ur.user_id INNER JOIN roles r ON " +
        "r.id = ur.role_id WHERE a.account_number = ?";
    try {
      return Optional.ofNullable(jdbcTemplate.queryForObject(sql,
          new Object[]{accountNumber},
          new AccountRowMapper()));
    } catch (EmptyResultDataAccessException e) {
      return Optional.empty();
    }
  }

  @Override
  public Account save(Account account) {
    Date now = new Date();

    String accountSql = "INSERT INTO accounts " +
        "(account_number, created_at, updated_at, " +
        "account_type, balance, credit_Score, user_id) " +
        "VALUES (?, ?, ?, ?, ?, ?, ?)";

    jdbcTemplate.update(accountSql, account.getAccountNumber(),
        new java.sql.Date(now.getTime()),
        new java.sql.Date(now.getTime()),
        account.getAccountType().name(),
        account.getBalance(),
        account.getCreditScore(),
        account.getUser().getId());

    return account;
  }

  @Override
  public Boolean update(Account account) {
    String sql = "UPDATE accounts SET " +
        "updated_at = ?, balance = ?, credit_score = ? " +
        "WHERE account_number = ?";
    int status = jdbcTemplate.update(sql,
        new Date(),
        account.getBalance(),
        account.getCreditScore(),
        account.getAccountNumber()
    );

    if (status == 0) {
      return false;
    }

    return true;
  }

}