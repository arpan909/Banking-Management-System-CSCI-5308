package asd.group2.bms.repository;

import asd.group2.bms.model.account.AccountActivity;

import java.util.Date;
import java.util.List;

public interface IAccountActivityRepository {

  /**
   * @param accountNumber: Account number of the user
   * @return list of all account activity by that account number between dates
   */
  List<AccountActivity> findAccountActivityByAccountNumber(Long accountNumber,
                                                           Date fromDate,
                                                           Date toDate);

  /**
   * @param accountActivity: Account activity details
   * @return This will return account activity if inserted in the database.
   */
  AccountActivity save(AccountActivity accountActivity);

  /**
   * @param accountActivity: Account activity details
   * @return true if account activity updated else false
   */
  Boolean update(AccountActivity accountActivity);

}
