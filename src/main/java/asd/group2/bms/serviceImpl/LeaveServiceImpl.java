package asd.group2.bms.serviceImpl;

import asd.group2.bms.exception.ResourceNotFoundException;
import asd.group2.bms.model.leaves.LeaveRequest;
import asd.group2.bms.model.leaves.RequestStatus;
import asd.group2.bms.model.user.User;
import asd.group2.bms.payload.response.ApiResponse;
import asd.group2.bms.payload.response.LeaveListResponse;
import asd.group2.bms.payload.response.PagedResponse;
import asd.group2.bms.repository.ILeaveRepository;
import asd.group2.bms.security.UserPrincipal;
import asd.group2.bms.service.ILeaveService;
import asd.group2.bms.util.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Service
public class LeaveServiceImpl implements ILeaveService {

  @Autowired
  ILeaveRepository leaveRepository;

  /**
   * @param requestStatus: Request Status (PENDING, APPROVED, REJECTED)
   * @param page:          Page Number
   * @param size:          Size of the response data
   * @return This will return all the leave request having status requestStatus
   */
  @Override
  public PagedResponse<LeaveListResponse> getLeavesByStatus(RequestStatus requestStatus, int page, int size) {
    // Making list in ascending order to get early applied application first
    Pageable pageable = PageRequest.of(page, size, Sort.Direction.ASC, "createdAt");
    Page<LeaveRequest> leaves = leaveRepository.findByRequestStatusEquals(requestStatus, pageable);

    if (leaves.getNumberOfElements() == 0) {
      return new PagedResponse<>(Collections.emptyList(), leaves.getNumber(),
          leaves.getSize(), leaves.getTotalElements(), leaves.getTotalPages(), leaves.isLast());
    }

    List<LeaveListResponse> leaveListResponses = leaves.map(ModelMapper::mapLeavesToLeaveListResponse).getContent();

    return new PagedResponse<>(leaveListResponses, leaves.getNumber(),
        leaves.getSize(), leaves.getTotalElements(), leaves.getTotalPages(), leaves.isLast());
  }

  /**
   * @param userId: id of the user
   * @return This will return all the leaves having user id userId
   */
  @Override
  public List<LeaveListResponse> getLeaveListByUserId(Long userId) {
    List<LeaveRequest> leaves = leaveRepository.findByUser_Id(userId);
    List<LeaveListResponse> leaveRequests = new ArrayList<>();
    leaves.forEach(leave -> leaveRequests.add(ModelMapper.mapLeavesToLeaveListResponse(leave)));
    return leaveRequests;
  }

  /**
   * Get the leave by leave id
   *
   * @param leaveId: id of the leave
   * @return a leave based on id
   */
  @Override
  public LeaveRequest getLeaveById(Long leaveId) {
    return leaveRepository.findById(leaveId).orElseThrow(() -> new ResourceNotFoundException("Leave ID", "leaveId", leaveId));
  }

  /**
   * @param leaveId:       id of the leave
   * @param requestStatus: Status of the leave (APPROVED, REJECTED, PENDING)
   * @return the updated status of the leave having id - leaveId
   */
  @Override
  public boolean setLeaveRequestStatus(Long leaveId, RequestStatus requestStatus) {
    LeaveRequest leaveRequest = getLeaveById(leaveId);
    leaveRequest.setRequestStatus(requestStatus);
    return leaveRepository.update(leaveRequest);
  }

  /**
   * @param currentUser: current logged in user
   * @param leaveId:     id of the leave
   * @return success or failure response with appropriate message
   */
  @Override
  public ResponseEntity<?> deleteLeaveRequestById(UserPrincipal currentUser, Long leaveId) {
    try {
      LeaveRequest leaveRequest = getLeaveById(leaveId);
      if (leaveRequest.getUser().getId().equals(currentUser.getId())) {
        leaveRepository.delete(leaveId);
        return ResponseEntity.ok(new ApiResponse(true, "Leave request deleted successfully"));
      }
      return new ResponseEntity<>(new ApiResponse(false, "You are not authorized to perform this operation"),
          HttpStatus.FORBIDDEN);

    } catch (Exception e) {
      return new ResponseEntity<>(new ApiResponse(false, "Something went wrong!"),
          HttpStatus.BAD_REQUEST);
    }
  }

  /**
   * This will create a leave request based on the field received from the frontend
   *
   * @param user:     User model object
   * @param fromDate: from date
   * @param toDate:   to date
   * @param reason:   reason of the leave
   * @return success or failure response with appropriate message
   */
  @Override
  public ResponseEntity<?> makeLeaveRequest(User user, Date fromDate, Date toDate, String reason) {
    try {
      LeaveRequest leaveRequest = new LeaveRequest(user, fromDate, toDate, reason,
          RequestStatus.PENDING);
      List<LeaveRequest> leavesList = leaveRepository.findByUser_Id(user.getId());
      if (leavesList.size() > 0) {
        for (LeaveRequest leaves : leavesList) {
          if (leaves.getRequestStatus() == RequestStatus.PENDING || leaves.getRequestStatus() == RequestStatus.APPROVED) {
            if ((!fromDate.before(leaves.getFromDate()) && !fromDate.after(leaves.getToDate())) || (!toDate.before(leaves.getFromDate()) && !toDate.after(leaves.getToDate()))) {
              return new ResponseEntity<>(new ApiResponse(false, "Dates overlapping with existing requests."),
                  HttpStatus.NOT_ACCEPTABLE);
            }
          }
        }
      }
      leaveRepository.save(leaveRequest);
      return ResponseEntity.ok(new ApiResponse(true, "Request made successfully!"));
    } catch (Exception e) {
      return new ResponseEntity<>(new ApiResponse(false, "Something went wrong!"),
          HttpStatus.BAD_REQUEST);
    }
  }

}
