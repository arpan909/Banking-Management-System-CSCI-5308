package asd.group2.bms.serviceImpl;

import asd.group2.bms.exception.ResourceNotFoundException;
import asd.group2.bms.model.resign.RequestStatus;
import asd.group2.bms.model.resign.ResignRequest;
import asd.group2.bms.model.user.User;
import asd.group2.bms.payload.response.ApiResponse;
import asd.group2.bms.payload.response.PagedResponse;
import asd.group2.bms.payload.response.ResignListResponse;
import asd.group2.bms.repository.IResignRepository;
import asd.group2.bms.security.UserPrincipal;
import asd.group2.bms.service.IResignService;
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
public class ResignServiceImpl implements IResignService {

  @Autowired
  IResignRepository resignRepository;

  /**
   * @param requestStatus: Resign Status (PENDING, APPROVED, REJECTED)
   * @param page:          Page Number
   * @param size:          Size of the response data
   * @return This will return all the resignations having status resignStatus
   */
  @Override
  public PagedResponse<ResignListResponse> getResignListByStatus(RequestStatus requestStatus, int page, int size) {
    // Making list in ascending order
    Pageable pageable = PageRequest.of(page, size, Sort.Direction.ASC, "createdAt");
    Page<ResignRequest> resigns = resignRepository.findByRequestStatusEquals(requestStatus, pageable);

    if (resigns.getNumberOfElements() == 0) {
      return new PagedResponse<>(Collections.emptyList(), resigns.getNumber(),
          resigns.getSize(), resigns.getTotalElements(), resigns.getTotalPages(), resigns.isLast());
    }

    List<ResignListResponse> resignListResponse = resigns.map(ModelMapper::mapResignsToResignListResponse).getContent();

    return new PagedResponse<>(resignListResponse, resigns.getNumber(),
        resigns.getSize(), resigns.getTotalElements(), resigns.getTotalPages(), resigns.isLast());
  }

  /**
   * @param userId: id of the user
   * @return This will return all the resignations having user id userId
   */
  @Override
  public List<ResignListResponse> getResignListByUserId(Long userId) {
    // Making list in ascending order
    List<ResignRequest> resigns = resignRepository.findByUser_Id(userId);
    List<ResignListResponse> resignRequests = new ArrayList<>();
    resigns.forEach(resign -> resignRequests.add(ModelMapper.mapResignsToResignListResponse(resign)));
    return resignRequests;
  }

  /**
   * Get the resign request by resign id
   *
   * @param resignId: id of the resign
   * @return a resign based on id
   */
  @Override
  public ResignRequest getResignById(Long resignId) {
    return resignRepository.findById(resignId).orElseThrow(() -> new ResourceNotFoundException("Resign ID", "resignId", resignId));
  }

  /**
   * @param resignId      :      id of the resign
   * @param requestStatus : Status of the resign (APPROVED, REJECTED, PENDING)
   * @return the updated status of the resign having id - resignId
   */
  @Override
  public boolean setResignRequestStatus(Long resignId, RequestStatus requestStatus) {
    ResignRequest resignRequest = getResignById(resignId);
    resignRequest.setRequestStatus(requestStatus);
    return resignRepository.update(resignRequest);
  }

  /**
   * @param currentUser: current logged in user
   * @param resignId:    id of the resign
   * @return success or failure response with appropriate message
   */
  @Override
  public ResponseEntity<?> deleteResignationRequestById(UserPrincipal currentUser, Long resignId) {
    try {
      ResignRequest resignRequest = getResignById(resignId);
      if (resignRequest.getUser().getId().equals(currentUser.getId())) {
        resignRepository.delete(resignId);
        return ResponseEntity.ok(new ApiResponse(true, "Resignation request deleted successfully"));
      }
      return new ResponseEntity<>(new ApiResponse(false, "You are not authorized to perform this operation"),
          HttpStatus.FORBIDDEN);
    } catch (Exception e) {
      return new ResponseEntity<>(new ApiResponse(false, "Something went wrong!"),
          HttpStatus.BAD_REQUEST);
    }
  }

  /**
   * This will create a resign request based on the field received from the frontend
   *
   * @param user:   User model object
   * @param date:   resign date
   * @param reason: reason of the resign
   * @return success or failure response with appropriate message
   */
  @Override
  public ResponseEntity<?> makeResignRequest(User user, Date date, String reason) {
    try {
      ResignRequest resignRequest = new ResignRequest(user, date, reason, RequestStatus.PENDING);
      List<ResignRequest> resignList = resignRepository.findByUserOrderByCreatedAtDesc(user);
      if (resignList.size() > 0) {
        ResignRequest lastRequest = resignList.get(0);
        RequestStatus lastStatus = lastRequest.getRequestStatus();
        if (lastStatus == RequestStatus.PENDING || lastStatus == RequestStatus.APPROVED) {
          return new ResponseEntity<>(new ApiResponse(false, "Resign request already " + lastStatus.toString().toLowerCase()),
              HttpStatus.NOT_ACCEPTABLE);
        }
      }
      resignRepository.save(resignRequest);
      return ResponseEntity.ok(new ApiResponse(true, "Request made successfully!"));
    } catch (Exception e) {
      return new ResponseEntity<>(new ApiResponse(false, "Something went wrong!"),
          HttpStatus.BAD_REQUEST);
    }
  }

}
