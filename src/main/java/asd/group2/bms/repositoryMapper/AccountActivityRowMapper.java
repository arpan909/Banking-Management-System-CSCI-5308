package asd.group2.bms.repositoryMapper;

import asd.group2.bms.model.account.Account;
import asd.group2.bms.model.account.AccountActivity;
import asd.group2.bms.model.account.ActivityType;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AccountActivityRowMapper implements RowMapper<AccountActivity> {

  @Override
  public AccountActivity mapRow(ResultSet resultSet, int i) throws SQLException {
    AccountActivity accountActivity = new AccountActivity();
    CommonMapping commonMapping = new CommonMapping();
    AccountRowMapper accountRowMapper = new AccountRowMapper();

    accountActivity.setCreatedAt(commonMapping.mapCreatedAtOrUpdatedAt(resultSet.getDate("created_at").toLocalDate()));
    accountActivity.setUpdatedAt(commonMapping.mapCreatedAtOrUpdatedAt(resultSet.getDate("updated_at").toLocalDate()));
    accountActivity.setActivityId(resultSet.getLong("activity_id"));
    accountActivity.setActivityType(ActivityType.valueOf(resultSet.getString(
        "activity_type")));
    accountActivity.setTransactionAmount(resultSet.getDouble(
        "transaction_amount"));
    accountActivity.setComment(resultSet.getString("comment"));

    Account account = accountRowMapper.mapRow(resultSet, i);
    accountActivity.setAccount(account);

    return accountActivity;
  }

}
