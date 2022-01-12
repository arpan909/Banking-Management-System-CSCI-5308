package asd.group2.bms.repositoryImpl;

import asd.group2.bms.model.account.AccountActivity;
import asd.group2.bms.repository.IAccountActivityRepository;
import asd.group2.bms.repositoryMapper.AccountActivityRowMapper;
import org.springframework.dao.EmptyResultDataAccessException;
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

@Repository
public class AccountActivityRepositoryImpl extends JdbcDaoSupport implements IAccountActivityRepository {

  private final JdbcTemplate jdbcTemplate;

  final
  DataSource dataSource;

  public AccountActivityRepositoryImpl(JdbcTemplate jdbcTemplate, DataSource dataSource) {
    this.jdbcTemplate = jdbcTemplate;
    this.dataSource = dataSource;
  }

  @PostConstruct
  private void initialize() {
    setDataSource(dataSource);
  }

  @Override
  public List<AccountActivity> findAccountActivityByAccountNumber(Long accountNumber,
                                                                  Date fromDate, Date toDate) {
    String sql = "SELECT * FROM account_activities aa " +
        "INNER JOIN accounts a ON aa.account_number = a.account_number " +
        "INNER JOIN users u ON a.user_id = u.id " +
        "INNER JOIN user_roles ur ON u.id = ur.user_id " +
        "INNER JOIN roles ro ON ro.id = ur.role_id " +
        "WHERE aa.account_number = ? " +
        "AND NOT (aa.created_at > ? OR aa.created_at < ?)";

    try {
      return jdbcTemplate.query(sql, new Object[]{accountNumber, toDate, fromDate},
          new AccountActivityRowMapper());
    } catch (EmptyResultDataAccessException e) {
      return Collections.emptyList();
    }
  }

  @Override
  public AccountActivity save(AccountActivity accountActivity) {
    Date now = new Date();
    KeyHolder keyHolder = new GeneratedKeyHolder();

    String accountActivitySql = "INSERT INTO account_activities " +
        "(created_at, updated_at, activity_type, comment, transaction_amount," +
        " account_number)" +
        "VALUES (?, ?, ?, ?, ?, ?)";

    jdbcTemplate.update(
        connection -> {
          PreparedStatement ps =
              connection.prepareStatement(accountActivitySql,
                  Statement.RETURN_GENERATED_KEYS);
          ps.setDate(1, new java.sql.Date(now.getTime()));
          ps.setDate(2, new java.sql.Date(now.getTime()));
          ps.setString(3, accountActivity.getActivityType().name());
          ps.setString(4, accountActivity.getComment());
          ps.setDouble(5, accountActivity.getTransactionAmount());
          ps.setLong(6, accountActivity.getAccount().getAccountNumber());
          return ps;
        }, keyHolder);

    accountActivity.setActivityId(keyHolder.getKey().longValue());
    return accountActivity;
  }

  @Override
  public Boolean update(AccountActivity accountActivity) {
    String sql = "UPDATE account_activities SET " +
        "updated_at = ?, comment = ? WHERE activity_id = ?";
    int status = jdbcTemplate.update(sql,
        new Date(),
        accountActivity.getComment(),
        accountActivity.getActivityId()
    );

    if (status == 0) {
      return false;
    }

    return true;
  }

}
