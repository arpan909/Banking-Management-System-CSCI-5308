package asd.group2.bms.repositoryMapper;

import asd.group2.bms.model.account.Account;
import asd.group2.bms.model.account.AccountType;
import asd.group2.bms.model.user.User;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AccountRowMapper implements RowMapper<Account> {

  @Override
  public Account mapRow(ResultSet resultSet, int i) throws SQLException {
    Account account = new Account();
    CommonMapping commonMapping = new CommonMapping();
    UserRowMapper userRowMapper = new UserRowMapper();

    account.setCreatedAt(commonMapping.mapCreatedAtOrUpdatedAt(resultSet.getDate(
        "created_at").toLocalDate()));
    account.setUpdatedAt(commonMapping.mapCreatedAtOrUpdatedAt(resultSet.getDate(
        "updated_at").toLocalDate()));
    account.setAccountNumber(resultSet.getLong("account_number"));
    account.setAccountType(AccountType.valueOf(resultSet.getString(
        "account_type")));
    account.setBalance(resultSet.getDouble("balance"));
    account.setCreditScore(resultSet.getInt("credit_score"));

    User user = userRowMapper.mapRow(resultSet, i);

    account.setUser(user);

    return account;
  }

}
