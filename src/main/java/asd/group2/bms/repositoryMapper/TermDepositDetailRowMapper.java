package asd.group2.bms.repositoryMapper;

import asd.group2.bms.model.account.Account;
import asd.group2.bms.model.term_deposit.TermDepositDetail;
import asd.group2.bms.model.term_deposit.TermDepositStatus;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TermDepositDetailRowMapper implements RowMapper<TermDepositDetail> {

  @Override
  public TermDepositDetail mapRow(ResultSet resultSet, int i) throws SQLException {
    TermDepositDetail termDepositDetail = new TermDepositDetail();
    CommonMapping commonMapping = new CommonMapping();
    AccountRowMapper accountRowMapper = new AccountRowMapper();

    termDepositDetail.setCreatedAt(commonMapping.mapCreatedAtOrUpdatedAt(resultSet.getDate(
        "created_at").toLocalDate()));
    termDepositDetail.setUpdatedAt(commonMapping.mapCreatedAtOrUpdatedAt(resultSet.getDate(
        "updated_at").toLocalDate()));
    termDepositDetail.setTermDepositId(resultSet.getLong("term_deposit_id"));
    termDepositDetail.setTermDepositStatus(TermDepositStatus.valueOf(resultSet.getString(
        "term_deposit_status")));
    termDepositDetail.setDuration(resultSet.getInt("duration"));
    termDepositDetail.setInitialAmount(resultSet.getDouble("initial_amount"));
    termDepositDetail.setMaturityAmount(resultSet.getDouble("maturity_amount"));
    termDepositDetail.setMaturityDate(resultSet.getDate("maturity_date"));
    termDepositDetail.setStartDate(resultSet.getDate("start_date"));
    termDepositDetail.setRateOfInterest(resultSet.getFloat("rate_of_interest"));

    Account account = accountRowMapper.mapRow(resultSet, i);
    termDepositDetail.setAccount(account);

    return termDepositDetail;

  }

}
