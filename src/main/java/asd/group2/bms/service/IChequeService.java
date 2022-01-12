package asd.group2.bms.service;

import asd.group2.bms.model.cheque.ChequeStatus;

public interface IChequeService {

  Boolean startCheque(Long senderAccountNumber,
                      Long receiverAccountNumber,
                      Long chequeNumber, Double chequeAmount);

  void processCheque(Long chequeNumber, Long senderAccountNumber,
                     Long receiverAccountNumber, Double chequeAmount);

  ChequeStatus chequeStatus(Long chequeNumber);

}
