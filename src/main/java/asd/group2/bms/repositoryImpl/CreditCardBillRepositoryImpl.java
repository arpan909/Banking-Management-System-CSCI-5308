package asd.group2.bms.repositoryImpl;

import asd.group2.bms.model.cards.credit.BillStatus;
import asd.group2.bms.model.cards.credit.CreditCardBill;
import asd.group2.bms.repository.ICreditCardBillRepository;
import asd.group2.bms.repositoryMapper.CreditCardBillMapper;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.util.Date;
import java.util.Optional;

@Repository
public class CreditCardBillRepositoryImpl extends JdbcDaoSupport implements ICreditCardBillRepository {

  private final JdbcTemplate jdbcTemplate;

  final
  DataSource dataSource;

  public CreditCardBillRepositoryImpl(JdbcTemplate jdbcTemplate, DataSource dataSource) {
    this.jdbcTemplate = jdbcTemplate;
    this.dataSource = dataSource;
  }

  @PostConstruct
  private void initialize() {
    setDataSource(dataSource);
  }

  @Override
  public Long getCreditCardNumber(Long accountNumber) {
    String sql = "select credit_card_number from credit_cards where account_number = ?";
    Long credit_card_number = jdbcTemplate.queryForObject(sql, new Object[]{accountNumber}, Long.class);
    return credit_card_number;
  }

  @Override
  public double getBillAmount(Long billId) {
    String getBillAmount = "select amount from credit_card_bills where bill_id = ?'";
    return jdbcTemplate.queryForObject(getBillAmount, new Object[]{billId}, Double.class);
  }


  @Override
  public Boolean payCreditCardBill(Long billId) {
    BillStatus paid = BillStatus.PAID;
    String UpdateBillStatus = "UPDATE credit_card_bills SET updated_at = ?,bill_status = ? where bill_id = ?'";
    int status = jdbcTemplate.update(UpdateBillStatus, new Date(), paid.name(), billId);
    return status != 0;

  }

  @Override
  public Optional<CreditCardBill> showBills(Long creditCardNo) {
    String billData = "select * from credit_card_bills where " +
        "credit_card_no = ?";
    try {
      return Optional.ofNullable(jdbcTemplate.queryForObject(billData,
          new Object[]{creditCardNo},
          new CreditCardBillMapper()));
    } catch (EmptyResultDataAccessException e) {
      return Optional.empty();
    }
  }

}
