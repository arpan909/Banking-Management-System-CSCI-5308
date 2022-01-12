package asd.group2.bms.service;

import asd.group2.bms.model.term_deposit.TermDepositDetail;
import org.springframework.http.ResponseEntity;

import java.util.Date;
import java.util.List;

public interface ITermDepositDetailService {

  ResponseEntity<?> makeTermDepositRequest(Long userId, String email, String firstName, Double fdAmount,
                                           Date currentDate, int duration) throws Exception;

  TermDepositDetail getTermDepositDetailById(Long id);

  List<TermDepositDetail> getTermDepositDetail(Long userId);

  Boolean closeTermDepositDetail(TermDepositDetail termDepositDetail);

  Boolean checkActiveTermDeposit(List<TermDepositDetail> termDepositDetailList);

}
