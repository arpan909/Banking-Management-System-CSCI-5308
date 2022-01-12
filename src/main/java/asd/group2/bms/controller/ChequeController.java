package asd.group2.bms.controller;


import asd.group2.bms.model.cheque.ChequeStatus;
import asd.group2.bms.payload.request.StartChequeRequest;
import asd.group2.bms.payload.response.ApiResponse;
import asd.group2.bms.service.IChequeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;

@RestController
@RequestMapping("/api")

public class ChequeController {

  @Autowired
  IChequeService chequeService;

  @PutMapping("/services/cheques")
  @RolesAllowed({"ROLE_MANAGER", "ROLE_EMPLOYEE"})
  public ResponseEntity<?> startCheque(@Valid @RequestBody StartChequeRequest startChequeRequest) {

    Boolean chequeStatus =
        chequeService.startCheque(startChequeRequest.getAccountNumberSender(), startChequeRequest.getAccountNumberReceiver(),
            startChequeRequest.getChequeNumber(),
            startChequeRequest.getAmount());
    chequeService.processCheque(startChequeRequest.getChequeNumber(),
        startChequeRequest.getAccountNumberSender(),
        startChequeRequest.getAccountNumberReceiver(),
        startChequeRequest.getAmount());
    if (chequeStatus) {
      return ResponseEntity.ok(new ApiResponse(true, "Bill paid successfully!"));
    } else {
      return new ResponseEntity<>(new ApiResponse(false, "Something went wrong while paying bill balance might be insufficient"),
          HttpStatus.BAD_REQUEST);
    }
  }

  @PutMapping("/services/cheques/chequestatus")
  @RolesAllowed({"ROLE_MANAGER", "ROLE_EMPLOYEE"})
  public ResponseEntity<?> chequeStatus(@RequestParam(value = "billId") Long chequeNumber) {
    ChequeStatus status = chequeService.chequeStatus(chequeNumber);
    if (status == ChequeStatus.CLEARED) {
      return ResponseEntity.ok(new ApiResponse(true, "Cheque cleared " +
          "Successfully"));
    } else {
      return new ResponseEntity<>(new ApiResponse(false, "Cheque " +
          "Bounced "),
          HttpStatus.BAD_REQUEST);
    }
  }

}
