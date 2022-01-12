package asd.group2.bms.serviceImpl;

import asd.group2.bms.model.account.Account;
import asd.group2.bms.model.cheque.ChequeStatus;
import asd.group2.bms.repository.IChequeRepository;
import asd.group2.bms.service.IAccountService;
import asd.group2.bms.service.IChequeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChequeServiceImpl implements IChequeService {

  @Autowired
  IChequeRepository chequeRepository;

  @Autowired
  IAccountService accountService;

  @Override
  public Boolean startCheque(Long senderAccountNumber,
                             Long receiverAccountNumber, Long chequeNumber, Double chequeAmount) {

    Boolean chequeStatus = chequeRepository.updateCheque(chequeNumber);
    Boolean chequeTransactionStatus =
        chequeRepository.updateChequeTransaction(senderAccountNumber,
            receiverAccountNumber, chequeNumber, chequeAmount);
    return chequeStatus && chequeTransactionStatus;

  }

  public void processCheque(Long chequeNumber, Long senderAccountNumber,
                            Long receiverAccountNumber, Double chequeAmount) {
    Account senderAccount = accountService.getAccountByAccountNumber(senderAccountNumber);
    Account receiverAccount =
        accountService.getAccountByAccountNumber(receiverAccountNumber);
    Double senderBalance = senderAccount.getBalance();
    Double receiverBalance = receiverAccount.getBalance();
    ChequeStatus chequeStatus;
    if (senderBalance >= chequeAmount) {
      senderAccount.setBalance(senderBalance - chequeAmount);
      receiverAccount.setBalance(receiverBalance + chequeAmount);
      accountService.updateAccountBalance(senderAccount);
      accountService.updateAccountBalance(receiverAccount);
      chequeStatus = ChequeStatus.CLEARED;
    } else {
      chequeStatus = ChequeStatus.RETURN;
    }
    chequeRepository.updateChequeStatus(chequeStatus);
  }

  @Override
  public ChequeStatus chequeStatus(Long chequeNumber) {
    return chequeRepository.getChequeStatus(chequeNumber);
  }


}
