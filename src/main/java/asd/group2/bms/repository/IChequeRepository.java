package asd.group2.bms.repository;

import asd.group2.bms.model.cheque.ChequeStatus;

public interface IChequeRepository {

  Boolean updateCheque(Long chequeNumber);

  Boolean updateChequeTransaction(Long senderAccountNumber,
                                  Long receiverAccountNumber, Long chequeNumber, Double chequeAmount);

  Boolean updateChequeStatus(ChequeStatus chequeStatus);

  ChequeStatus getChequeStatus(Long chequeNumber);

}
