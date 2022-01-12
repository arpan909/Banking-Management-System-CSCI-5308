package asd.group2.bms.service;

import asd.group2.bms.security.UserPrincipal;
import org.springframework.http.ResponseEntity;

import java.util.Date;

public interface IAccountActivityService {

  ResponseEntity<?> fundTransfer(Long senderAccountNumber,
                                 Long receiverAccountNumber,
                                 String comment, Double transactionAmount) throws Exception;

  ResponseEntity<?> getAccountActivity(UserPrincipal currentUser,
                                       Date fromDate,
                                       Date toDate);

}
