package asd.group2.bms.repositoryImpl;

import asd.group2.bms.model.cards.credit.CreditCard;
import asd.group2.bms.model.cards.credit.CreditCardStatus;
import asd.group2.bms.repository.ICreditCardRepository;
import asd.group2.bms.repositoryMapper.CreditCardRowMapper;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public class CreditCardRepositoryImpl extends JdbcDaoSupport implements ICreditCardRepository {

  private final JdbcTemplate jdbcTemplate;

  final
  DataSource dataSource;

  public CreditCardRepositoryImpl(JdbcTemplate jdbcTemplate, DataSource dataSource) {
    this.jdbcTemplate = jdbcTemplate;
    this.dataSource = dataSource;
  }

  @PostConstruct
  private void initialize() {
    setDataSource(dataSource);
  }


  @Override
  public Page<CreditCard> findByCreditCardStatusEquals(CreditCardStatus creditCardStatus, Pageable pageable) {
    String rowCountSql = "SELECT count(1) AS row_count FROM credit_cards WHERE credit_card_status = ?";

    int total =
        jdbcTemplate.queryForObject(
            rowCountSql,
            new Object[]{creditCardStatus.name()},
            (rs, rowNum) -> rs.getInt(1)
        );

    String querySql = "SELECT * FROM credit_cards cc INNER JOIN accounts a ON cc.account_number = a.account_number " +
        "INNER JOIN users u ON a.user_id = u.id INNER JOIN user_roles ur ON u.id = ur.user_id " +
        "INNER JOIN roles r ON r.id = ur.role_id WHERE cc.credit_card_status = ? " +
        "LIMIT " + pageable.getPageSize() + " OFFSET " + pageable.getOffset();

    List<CreditCard> creditCards = jdbcTemplate.query(
        querySql,
        new Object[]{creditCardStatus.name()}, new CreditCardRowMapper()
    );

    return new PageImpl<>(creditCards, pageable, total);
  }

  @Override
  public Optional<CreditCard> findById(Long creditCardNumber) {
    String sql = "SELECT * FROM credit_cards cc INNER JOIN accounts a ON cc.account_number = a.account_number " +
        "INNER JOIN users u ON a.user_id = u.id INNER JOIN user_roles ur ON " +
        "u.id = ur.user_id INNER JOIN roles r ON r.id = ur.role_id WHERE cc.credit_card_number = ?";

    try {
      return Optional.ofNullable(jdbcTemplate.queryForObject(sql,
          new Object[]{creditCardNumber},
          new CreditCardRowMapper()));
    } catch (EmptyResultDataAccessException e) {
      return Optional.empty();
    }
  }

  @Override
  public CreditCard save(CreditCard creditCard) {
    Date now = new Date();

    String creditCardSql = "INSERT INTO credit_cards " +
        "(credit_card_number, created_at, updated_at, " +
        "credit_card_status, is_active, pin, transaction_limit, " +
        "account_number, cvv, expiry_month, expiry_year) " +
        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    jdbcTemplate.update(creditCardSql, creditCard.getCreditCardNumber(),
        new java.sql.Date(now.getTime()),
        new java.sql.Date(now.getTime()),
        creditCard.getCreditCardStatus().name(),
        creditCard.getActive(),
        creditCard.getPin(),
        creditCard.getTransactionLimit(),
        creditCard.getAccount().getAccountNumber(),
        creditCard.getCvv(),
        creditCard.getExpiryMonth(),
        creditCard.getExpiryYear());

    creditCard.setCreatedAt(now.toInstant());
    creditCard.setUpdatedAt(now.toInstant());

    return creditCard;
  }

  @Override
  public Boolean update(CreditCard creditCard) {
    String sql = "UPDATE credit_cards SET " +
        "updated_at = ?, credit_card_status = ?, is_active = ?, pin = ?, " +
        "transaction_limit = ? WHERE credit_card_number = ?";
    int status = jdbcTemplate.update(sql,
        new Date(),
        creditCard.getCreditCardStatus().name(),
        creditCard.getActive(),
        creditCard.getPin(),
        creditCard.getTransactionLimit(),
        creditCard.getCreditCardNumber()
    );

    if (status == 0) {
      return false;
    }

    return true;
  }

}
