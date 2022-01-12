package asd.group2.bms.service;

import asd.group2.bms.model.leaves.LeaveRequest;
import asd.group2.bms.model.leaves.RequestStatus;
import asd.group2.bms.model.user.User;
import asd.group2.bms.payload.response.LeaveListResponse;
import asd.group2.bms.payload.response.PagedResponse;
import asd.group2.bms.security.UserPrincipal;
import org.springframework.http.ResponseEntity;

import java.util.Date;
import java.util.List;

public interface ILeaveService {

  PagedResponse<LeaveListResponse> getLeavesByStatus(RequestStatus requestStatus, int page, int size);

  List<LeaveListResponse> getLeaveListByUserId(Long userId);

  LeaveRequest getLeaveById(Long leaveId);

  boolean setLeaveRequestStatus(Long leaveId, RequestStatus requestStatus);

  ResponseEntity<?> deleteLeaveRequestById(UserPrincipal currentUser,
                                           Long leaveId);

  ResponseEntity<?> makeLeaveRequest(User user, Date fromDate, Date toDate,
                                     String reason);

}
