package asd.group2.bms.repositoryImpl;

import asd.group2.bms.model.term_deposit.TermDepositDetail;
import asd.group2.bms.repository.ITermDepositDetailRepository;
import asd.group2.bms.repositoryMapper.TermDepositDetailRowMapper;
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
import java.util.Optional;


@Repository
public class TermDepositDetailRepositoryImpl extends JdbcDaoSupport implements ITermDepositDetailRepository {

  private final JdbcTemplate jdbcTemplate;

  final
  DataSource dataSource;

  public TermDepositDetailRepositoryImpl(JdbcTemplate jdbcTemplate, DataSource dataSource) {
    this.jdbcTemplate = jdbcTemplate;
    this.dataSource = dataSource;
  }

  @PostConstruct
  private void initialize() {
    setDataSource(dataSource);
  }

  /**
   * @param accountNumber: bank account number of the user
   * @return This will return the user's bank account number.
   */
  public List<TermDepositDetail> findTermDepositDetailByAccount_AccountNumber(Long accountNumber) {
    String sql = "SELECT * FROM term_deposit_details td INNER JOIN accounts a" +
        " ON a.account_number = td.account_number INNER JOIN users u ON u.id " +
        "= a.user_id INNER JOIN user_roles ur on u.id = ur.user_id INNER JOIN " +
        "roles r ON r.id = ur.role_id WHERE a.account_number= ?";
    try {
      return jdbcTemplate.query(sql, new Object[]{accountNumber}, new TermDepositDetailRowMapper());
    } catch (EmptyResultDataAccessException e) {
      return Collections.emptyList();
    }
  }

  @Override
  public Optional<TermDepositDetail> findById(Long termDepositId) {
    String sql = "SELECT * FROM term_deposit_details td INNER JOIN accounts a" +
        " ON a.account_number = td.account_number " +
        "INNER JOIN users u on u.id = a.user_id " +
        "INNER JOIN user_roles ur on u.id = ur.user_id " +
        "INNER JOIN roles r ON r.id = ur.role_id " +
        "WHERE td.term_deposit_id = ?";

    try {
      return Optional.ofNullable(jdbcTemplate.queryForObject(sql,
          new Object[]{termDepositId},
          new TermDepositDetailRowMapper()));
    } catch (EmptyResultDataAccessException e) {
      return Optional.empty();
    }
  }

  @Override
  public TermDepositDetail save(TermDepositDetail termDepositDetail) {
    Date now = new Date();
    KeyHolder keyHolder = new GeneratedKeyHolder();

    String termDepositSql = "INSERT INTO term_deposit_details " +
        "(created_at, updated_at, duration, initial_amount, " +
        "maturity_amount, maturity_date, rate_of_interest, " +
        "start_date, term_deposit_status, account_number) " +
        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    jdbcTemplate.update(
        connection -> {
          PreparedStatement ps =
              connection.prepareStatement(termDepositSql,
                  Statement.RETURN_GENERATED_KEYS);
          ps.setDate(1, new java.sql.Date(now.getTime()));
          ps.setDate(2, new java.sql.Date(now.getTime()));
          ps.setInt(3, termDepositDetail.getDuration());
          ps.setDouble(4, termDepositDetail.getInitialAmount());
          ps.setDouble(5, termDepositDetail.getMaturityAmount());
          ps.setDate(6,
              new java.sql.Date(termDepositDetail.getMaturityDate().getTime()));
          ps.setFloat(7, termDepositDetail.getRateOfInterest());
          ps.setDate(8, new java.sql.Date(termDepositDetail.getStartDate().getTime()));
          ps.setString(9, termDepositDetail.getTermDepositStatus().name());
          ps.setLong(10, termDepositDetail.getAccount().getAccountNumber());
          return ps;
        }, keyHolder);

    return termDepositDetail;
  }

  @Override
  public Boolean update(TermDepositDetail termDepositDetail) {
    String sql = "UPDATE term_deposit_details SET duration = ?, " +
        "initial_amount = ?, maturity_amount = ?, maturity_date = ?, " +
        "rate_of_interest = ?, term_deposit_status = ? WHERE term_deposit_id = ?";
    int status = jdbcTemplate.update(sql,
        termDepositDetail.getDuration(),
        termDepositDetail.getInitialAmount(),
        termDepositDetail.getMaturityAmount(),
        termDepositDetail.getMaturityDate(),
        termDepositDetail.getRateOfInterest(),
        termDepositDetail.getTermDepositStatus().name(),
        termDepositDetail.getTermDepositId());

    if (status == 0) {
      return false;
    }

    return true;
  }

}