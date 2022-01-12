package asd.group2.bms.service;

import asd.group2.bms.model.cards.credit.CreditCardBill;

import java.util.Optional;

public interface ICreditCardBillService {

  Boolean payCreditCardBill(Long accountNumber, Long billId);


  Optional<CreditCardBill> getBills(Long creditCardNo);

}
