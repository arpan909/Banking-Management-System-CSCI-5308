package asd.group2.bms.repository;

import asd.group2.bms.model.leaves.LeaveRequest;
import asd.group2.bms.model.leaves.RequestStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ILeaveRepository {

  /**
   * @param requestStatus: request status
   * @return This will return list of leaves having request status of param - requestStatus.
   */
  Page<LeaveRequest> findByRequestStatusEquals(RequestStatus requestStatus, Pageable pageable);

  /**
   * @param userId: Id of user
   * @return This will return the leave records by user id.
   */
  List<LeaveRequest> findByUser_Id(Long userId);

  /**
   * @param leaveId: leave id
   * @return This will return leave request based on leave id.
   */
  Optional<LeaveRequest> findById(Long leaveId);

//  /**
//   * @param user: User model object
//   * @return This will return the leave record by user.
//   */
//  List<LeaveRequest> findByUser(User user);

  /*
    @param leaveIds: Leave Ids
   * @return This will return the leaves records by leave ids.
   */
  // List<LeaveRequest> findByLeaveIdIn(List<Long> leaveIds);

  /*
    @return This will return the leaves records
   */
  // List<LeaveRequest> findAll();

  /**
   * @param leaveRequest: LeaveRequest details
   * @return This will return leaveRequest if inserted in the database.
   */
  LeaveRequest save(LeaveRequest leaveRequest);

  /**
   * @param leaveRequest: LeaveRequest details
   * @return true if leaveRequest updated else false
   */
  Boolean update(LeaveRequest leaveRequest);

  /**
   * @param leaveId: Leave Id to be deleted
   */
  void delete(Long leaveId);

}
