package asd.group2.bms.repositoryMapper;

import asd.group2.bms.model.account.Account;
import asd.group2.bms.model.cards.credit.CreditCard;
import asd.group2.bms.model.cards.credit.CreditCardStatus;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CreditCardRowMapper implements RowMapper<CreditCard> {

  @Override
  public CreditCard mapRow(ResultSet resultSet, int i) throws SQLException {
    CreditCard creditCard = new CreditCard();
    CommonMapping commonMapping = new CommonMapping();
    AccountRowMapper accountRowMapper = new AccountRowMapper();

    creditCard.setCreatedAt(commonMapping.mapCreatedAtOrUpdatedAt(resultSet.getDate(
        "created_at").toLocalDate()));
    creditCard.setUpdatedAt(commonMapping.mapCreatedAtOrUpdatedAt(resultSet.getDate(
        "updated_at").toLocalDate()));
    creditCard.setCreditCardNumber(resultSet.getLong("credit_card_number"));
    creditCard.setCreditCardStatus(CreditCardStatus.valueOf(resultSet.getString(
        "credit_card_status")));
    creditCard.setActive(resultSet.getBoolean("is_active"));
    creditCard.setPin(resultSet.getString("pin"));
    creditCard.setTransactionLimit(resultSet.getInt("transaction_limit"));
    creditCard.setCvv(resultSet.getString("cvv"));
    creditCard.setExpiryMonth(resultSet.getString("expiry_month"));
    creditCard.setExpiryYear(resultSet.getString("expiry_year"));

    Account account = accountRowMapper.mapRow(resultSet, i);

    creditCard.setAccount(account);

    return creditCard;
  }

}
