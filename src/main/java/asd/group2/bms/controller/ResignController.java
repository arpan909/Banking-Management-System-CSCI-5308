package asd.group2.bms.controller;

import asd.group2.bms.model.resign.RequestStatus;
import asd.group2.bms.model.user.User;
import asd.group2.bms.payload.request.ResignRequest;
import asd.group2.bms.payload.request.UpdateResignStatusRequest;
import asd.group2.bms.payload.response.ApiResponse;
import asd.group2.bms.payload.response.PagedResponse;
import asd.group2.bms.payload.response.ResignListResponse;
import asd.group2.bms.security.CurrentLoggedInUser;
import asd.group2.bms.security.UserPrincipal;
import asd.group2.bms.service.IResignService;
import asd.group2.bms.service.IUserService;
import asd.group2.bms.util.AppConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ResignController {

  @Autowired
  IResignService resignService;

  @Autowired
  IUserService userService;

  /**
   * @param requestStatus: resign request status
   * @param page:          number of the page
   * @param size:page      size
   * @return all the request having status requestStatus
   */
  @GetMapping("/staff/resignation")
  @RolesAllowed({"ROLE_MANAGER", "ROLE_HR"})
  public PagedResponse<ResignListResponse> getResignationByStatus(
      @RequestParam(value = "requestStatus") RequestStatus requestStatus,
      @RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
      @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size) {
    return resignService.getResignListByStatus(requestStatus, page, size);
  }

  /**
   * @param userId: id of the user
   * @return Return all the request having user id - userId
   */
  @GetMapping("/staff/resignation/user/{userId}")
  @RolesAllowed({"ROLE_MANAGER", "ROLE_HR", "ROLE_EMPLOYEE"})
  public List<ResignListResponse> getResignationsByUserId(@PathVariable(value = "userId") Long userId) {
    return resignService.getResignListByUserId(userId);
  }

  /**
   * Delete resign request having resign id - resignId
   *
   * @param resignId: resign id to be deleted
   * @return success or failure response with appropriate message
   */
  @DeleteMapping("/staff/resignation/{resignId}")
  @RolesAllowed({"ROLE_MANAGER", "ROLE_HR", "ROLE_EMPLOYEE"})
  public ResponseEntity<?> deleteResignationRequestById(@CurrentLoggedInUser UserPrincipal currentUser, @PathVariable(value = "resignId") Long resignId) {
    return resignService.deleteResignationRequestById(currentUser, resignId);
  }

  /**
   * It will create a resign request
   *
   * @param resignRequest: resign request of user
   * @return success or failure response with appropriate message
   */
  @PostMapping("/staff/resignation")
  @RolesAllowed({"ROLE_MANAGER", "ROLE_HR", "ROLE_EMPLOYEE"})
  public ResponseEntity<?> makeResignRequest(@CurrentLoggedInUser UserPrincipal currentUser, @Valid @RequestBody ResignRequest resignRequest) {
    String email = currentUser.getEmail();
    User user = userService.getUserByEmail(email);
    Date date = resignRequest.getDate();
    String reason = resignRequest.getReason();

    return resignService.makeResignRequest(user, date, reason);
  }

  /**
   * Update the resign status
   *
   * @param updateResignStatusRequest: resign id and request status
   * @return success or failure response with appropriate message
   */
  @PutMapping("/staff/resignation")
  @RolesAllowed({"ROLE_HR", "ROLE_MANAGER"})
  public ResponseEntity<?> updateResignRequestStatus(
      @Valid @RequestBody UpdateResignStatusRequest updateResignStatusRequest) {
    boolean isUpdated = resignService.setResignRequestStatus(updateResignStatusRequest.getResignId(), updateResignStatusRequest.getRequestStatus());
    if (isUpdated) {
      return ResponseEntity.ok(new ApiResponse(true, "Resign request status changed successfully!"));
    } else {
      return new ResponseEntity<>(new ApiResponse(false, "Something went wrong while changing resign request status!"),
          HttpStatus.BAD_REQUEST);
    }
  }

}
