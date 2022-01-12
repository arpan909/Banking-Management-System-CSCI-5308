package asd.group2.bms.controller;

import asd.group2.bms.payload.request.FundTransferRequest;
import asd.group2.bms.security.CurrentLoggedInUser;
import asd.group2.bms.security.UserPrincipal;
import asd.group2.bms.service.IAccountActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import java.util.Date;

@RestController
@RequestMapping("/api")
public class AccountActivityController {

  @Autowired
  IAccountActivityService accountActivityService;

  /**
   * @param fundTransferRequest: Fund transfer request body that contains the
   *                             transaction related information
   * @return success or failure based on execution
   * @throws Exception Any exception that might happen during transaction
   */
  @PostMapping("/account/activity")
  @RolesAllowed({"ROLE_USER"})
  public ResponseEntity<?> fundTransfer(@Valid @RequestBody FundTransferRequest fundTransferRequest) throws Exception {
    Long senderAccountNumber = fundTransferRequest.getSenderAccountNumber();
    Long receiverAccountNumber = fundTransferRequest.getReceiverAccountNumber();
    String comment = fundTransferRequest.getComment();
    Double transactionAmount = fundTransferRequest.getTransactionAmount();

    return accountActivityService.fundTransfer(senderAccountNumber,
        receiverAccountNumber, comment, transactionAmount);
  }

  /**
   * @param currentUser: Current logged in user
   * @param fromDate:    From date
   * @param toDate:      To date
   * @return Account statement data to the user
   */
  @GetMapping("/account/activity")
  @RolesAllowed({"ROLE_USER"})
  public ResponseEntity<?> getAccountStatement(@CurrentLoggedInUser UserPrincipal currentUser,
                                               @RequestParam(value = "fromDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date fromDate,
                                               @RequestParam(value = "toDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date toDate) {

    return accountActivityService.getAccountActivity(currentUser, fromDate, toDate);
  }

}
