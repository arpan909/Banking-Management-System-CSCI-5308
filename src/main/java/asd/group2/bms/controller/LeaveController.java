package asd.group2.bms.controller;

import asd.group2.bms.model.leaves.RequestStatus;
import asd.group2.bms.model.user.User;
import asd.group2.bms.payload.request.UpdateLeaveStatusRequest;
import asd.group2.bms.payload.response.ApiResponse;
import asd.group2.bms.payload.response.LeaveListResponse;
import asd.group2.bms.payload.response.PagedResponse;
import asd.group2.bms.security.CurrentLoggedInUser;
import asd.group2.bms.security.UserPrincipal;
import asd.group2.bms.service.ILeaveService;
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
public class LeaveController {

  @Autowired
  IUserService userService;

  @Autowired
  ILeaveService leaveService;

  /**
   * Leaves list based on the leave status of the users
   *
   * @param requestStatus: leave request status
   * @param page:          number of the page
   * @param size:page      size
   * @return all the request having status requestStatus
   */
  @GetMapping("/staff/leave")
  @RolesAllowed({"ROLE_HR", "ROLE_MANAGER"})
  public PagedResponse<LeaveListResponse> getLeavesByStatus(@RequestParam(value = "requestStatus") RequestStatus requestStatus,
                                                            @RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
                                                            @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size) {
    return leaveService.getLeavesByStatus(requestStatus, page, size);
  }

  /**
   * @param userId : id of the user
   * @return all the request having user id - userId
   */
  @GetMapping("/staff/leave/user/{userId}")
  @RolesAllowed({"ROLE_MANAGER", "ROLE_HR", "ROLE_EMPLOYEE"})
  public List<LeaveListResponse> getLeavesByUserId(@PathVariable(value = "userId") Long userId) {
    return leaveService.getLeaveListByUserId(userId);
  }

  /**
   * Delete leave request having leave id - leaveId
   *
   * @param currentUser: current logged in user
   * @param leaveId:     leave id to be deleted
   * @return success or failure response with appropriate message
   */
  @DeleteMapping("/staff/leave/{leaveId}")
  @RolesAllowed({"ROLE_MANAGER", "ROLE_HR", "ROLE_EMPLOYEE"})
  public ResponseEntity<?> deleteLeaveRequestById(@CurrentLoggedInUser UserPrincipal currentUser, @PathVariable(value = "leaveId") Long leaveId) {
    return leaveService.deleteLeaveRequestById(currentUser, leaveId);
  }

  /**
   * @param currentUser:  current logged in user
   * @param leaveRequest: necessary field to make a leave request
   * @return success or failure response with appropriate message
   */
  @PostMapping("/staff/leave")
  @RolesAllowed({"ROLE_EMPLOYEE", "ROLE_MANAGER", "ROLES_HR"})
  public ResponseEntity<?> makeLeaveRequest(@CurrentLoggedInUser UserPrincipal currentUser, @Valid @RequestBody asd.group2.bms.payload.request.LeaveRequest leaveRequest) {
    String email = currentUser.getEmail();
    User user = userService.getUserByEmail(email);
    Date fromDate = leaveRequest.getFromDate();
    Date toDate = leaveRequest.getToDate();
    String reason = leaveRequest.getReason();

    return leaveService.makeLeaveRequest(user, fromDate, toDate, reason);
  }

  /**
   * Update the leave status
   *
   * @param updateLeaveStatusRequest: leave id and request status
   * @return success or failure response with appropriate message
   */
  @PutMapping("/staff/leave")
  @RolesAllowed({"ROLE_HR", "ROLE_MANAGER"})
  public ResponseEntity<?> updateLeaveRequestStatus(
      @Valid @RequestBody UpdateLeaveStatusRequest updateLeaveStatusRequest) {
    boolean isUpdated = leaveService.setLeaveRequestStatus(updateLeaveStatusRequest.getLeaveId(), updateLeaveStatusRequest.getRequestStatus());
    if (isUpdated) {
      return ResponseEntity.ok(new ApiResponse(true, "Leave request status changed successfully!"));
    } else {
      return new ResponseEntity<>(new ApiResponse(false, "Something went wrong while changing leave request status!"),
          HttpStatus.BAD_REQUEST);
    }
  }

}
