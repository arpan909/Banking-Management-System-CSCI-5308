package asd.group2.bms.repositoryMapper;

import asd.group2.bms.model.cards.credit.BillStatus;
import asd.group2.bms.model.cards.credit.CreditCard;
import asd.group2.bms.model.cards.credit.CreditCardBill;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CreditCardBillMapper implements RowMapper<CreditCardBill> {

  @Override
  public CreditCardBill mapRow(ResultSet resultSet, int i) throws SQLException {
    CreditCardRowMapper creditCardRowMapper = new CreditCardRowMapper();

    CommonMapping commonMapping = new CommonMapping();
    CreditCardBill creditCardBill = new CreditCardBill();

    creditCardBill.setCreatedAt(commonMapping.mapCreatedAtOrUpdatedAt(resultSet.getDate(
        "created_at").toLocalDate()));
    creditCardBill.setUpdatedAt(commonMapping.mapCreatedAtOrUpdatedAt(resultSet.getDate(
        "updated_at").toLocalDate()));
    creditCardBill.setBillId(resultSet.getLong(
        "bill_id"));
    creditCardBill.setAmount(resultSet.getDouble("amount"));
    creditCardBill.setBillStatus(BillStatus.valueOf(resultSet.getString("pin")));
    creditCardBill.setDueDate(resultSet.getDate("due_date"));
    CreditCard creditCard = creditCardRowMapper.mapRow(resultSet, i);
    creditCardBill.setCreditCard(creditCard);

    return creditCardBill;
  }

}
