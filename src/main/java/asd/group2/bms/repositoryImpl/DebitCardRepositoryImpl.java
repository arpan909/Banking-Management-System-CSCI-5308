package asd.group2.bms.repositoryImpl;

import asd.group2.bms.model.cards.debit.DebitCard;
import asd.group2.bms.repository.IDebitCardRepository;
import asd.group2.bms.repositoryMapper.DebitCardRowMapper;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.util.Date;
import java.util.Optional;

@Repository
public class DebitCardRepositoryImpl extends JdbcDaoSupport implements IDebitCardRepository {

  private final JdbcTemplate jdbcTemplate;

  final
  DataSource dataSource;

  public DebitCardRepositoryImpl(JdbcTemplate jdbcTemplate, DataSource dataSource) {
    this.jdbcTemplate = jdbcTemplate;
    this.dataSource = dataSource;
  }

  @PostConstruct
  private void initialize() {
    setDataSource(dataSource);
  }


  @Override
  public Optional<DebitCard> findById(Long debitCardNumber) {

    String sql = "SELECT * FROM debit_cards dc INNER JOIN accounts a ON dc.account_number = a.account_number " +
        "INNER JOIN users u ON a.user_id = u.id INNER JOIN user_roles ur ON " +
        "u.id = ur.user_id INNER JOIN roles r ON r.id = ur.role_id WHERE dc.debit_card_number = ?";

    try {
      return Optional.ofNullable(jdbcTemplate.queryForObject(sql,
          new Object[]{debitCardNumber},
          new DebitCardRowMapper()));
    } catch (EmptyResultDataAccessException e) {
      return Optional.empty();
    }
  }

  @Override
  public Optional<DebitCard> findByAccountNumber(Long accountNumber) {

    String sql = "SELECT * FROM debit_cards dc INNER JOIN accounts a ON dc.account_number = a.account_number " +
        "INNER JOIN users u ON a.user_id = u.id INNER JOIN user_roles ur ON " +
        "u.id = ur.user_id INNER JOIN roles r ON r.id = ur.role_id WHERE dc.account_number = ?";

    try {
      return Optional.ofNullable(jdbcTemplate.queryForObject(sql,
          new Object[]{accountNumber},
          new DebitCardRowMapper()));
    } catch (EmptyResultDataAccessException e) {
      return Optional.empty();
    }
  }

  @Override
  public DebitCard save(DebitCard debitCard) {

    Date now = new Date();

    String debitCardSql = "INSERT INTO debit_cards " +
        "(debit_card_number, created_at, updated_at," +
        "cvv, debit_card_status, expiry_month," +
        "expiry_year, pin, transaction_limit, account_number)" +
        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    jdbcTemplate.update(debitCardSql, debitCard.getDebitCardNumber(),
        new java.sql.Date(now.getTime()),
        new java.sql.Date(now.getTime()),
        debitCard.getCvv(),
        debitCard.getDebitCardStatus().name(),
        debitCard.getExpiryMonth(),
        debitCard.getExpiryYear(),
        debitCard.getPin(),
        debitCard.getTransactionLimit(),
        debitCard.getAccount().getAccountNumber());

    debitCard.setCreatedAt(now.toInstant());
    debitCard.setUpdatedAt(now.toInstant());

    return debitCard;
  }

  @Override
  public Boolean update(DebitCard debitCard) {
    String sql = "UPDATE debit_cards SET " +
        "updated_at = ?, debit_card_status = ? , pin = ?, " +
        "transaction_limit = ? WHERE debit_card_number = ?";
    int status = jdbcTemplate.update(sql,
        new Date(),
        debitCard.getDebitCardStatus().name(),
        debitCard.getPin(),
        debitCard.getTransactionLimit(),
        debitCard.getDebitCardNumber()
    );

    if (status == 0) {
      return false;
    }

    return true;
  }

}
