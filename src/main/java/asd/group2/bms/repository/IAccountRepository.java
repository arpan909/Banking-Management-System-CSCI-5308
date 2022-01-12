package asd.group2.bms.repository;

import asd.group2.bms.model.account.Account;

import java.util.Optional;

public interface IAccountRepository {

  /**
   * @param userId: user id of the user
   * @return This will return the account detail of the user
   */
  Optional<Account> findAccountByUser_Id(Long userId);

  /**
   * @param accountNumber: account number of user whose account detail is
   *                       requested
   * @return This will return the account details of the user based on
   * account number
   */
  Optional<Account> findAccountByAccountNumber(Long accountNumber);

  /**
   * @param account: Account details
   * @return This will return account if inserted in the database.
   */
  Account save(Account account);

  /**
   * @param account: Account details
   * @return true if account updated else false
   */
  Boolean update(Account account);

}
