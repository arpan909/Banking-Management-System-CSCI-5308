package asd.group2.bms.repository;

import asd.group2.bms.model.term_deposit.TermDepositDetail;

import java.util.List;
import java.util.Optional;

public interface ITermDepositDetailRepository {

  /**
   * @param accountNumber: bank account number of the user
   * @return This will return the user's bank account number.
   */
  List<TermDepositDetail> findTermDepositDetailByAccount_AccountNumber(Long accountNumber);

  /**
   * @param termDepositId: term deposit id
   * @return This will return termDeposit request based on termDeposit id.
   */
  Optional<TermDepositDetail> findById(Long termDepositId);

  /**
   * @param termDepositDetail: TermDepositDetail details
   * @return This will return termDepositDetail if inserted in the database.
   */
  TermDepositDetail save(TermDepositDetail termDepositDetail);

  /**
   * @param termDepositDetail: TermDepositDetail details
   * @return true if termDepositDetail updated else false
   */
  Boolean update(TermDepositDetail termDepositDetail);

}
