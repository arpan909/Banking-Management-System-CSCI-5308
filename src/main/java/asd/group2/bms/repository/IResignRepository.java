package asd.group2.bms.repository;

import asd.group2.bms.model.resign.RequestStatus;
import asd.group2.bms.model.resign.ResignRequest;
import asd.group2.bms.model.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface IResignRepository {

  /**
   * @param requestStatus: request status
   * @return This will return the resign records by status.
   */
  Page<ResignRequest> findByRequestStatusEquals(RequestStatus requestStatus, Pageable pageable);

  /**
   * @param userId: Id of user
   * @return This will return the resign records by user id.
   */
  List<ResignRequest> findByUser_Id(Long userId);

  /**
   * @param resignId: resign id
   * @return This will return resign request based on resign id.
   */
  Optional<ResignRequest> findById(Long resignId);

  /**
   * @param user: User model object
   * @return This will return the resign records by user.
   */
  List<ResignRequest> findByUserOrderByCreatedAtDesc(User user);

  /**
   * @param resignRequest: ResignRequest details
   * @return This will return resignRequest if inserted in the database.
   */
  ResignRequest save(ResignRequest resignRequest);

  /**
   * @param resignRequest: ResignRequest details
   * @return true if resignRequest updated else false
   */
  Boolean update(ResignRequest resignRequest);

  /**
   * @param resignId: Resign request id to be deleted
   */
  void delete(Long resignId);

}
