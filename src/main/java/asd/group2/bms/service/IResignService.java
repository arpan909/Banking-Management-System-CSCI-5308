package asd.group2.bms.service;

import asd.group2.bms.model.resign.RequestStatus;
import asd.group2.bms.model.resign.ResignRequest;
import asd.group2.bms.model.user.User;
import asd.group2.bms.payload.response.PagedResponse;
import asd.group2.bms.payload.response.ResignListResponse;
import asd.group2.bms.security.UserPrincipal;
import org.springframework.http.ResponseEntity;

import java.util.Date;
import java.util.List;

public interface IResignService {

  PagedResponse<ResignListResponse> getResignListByStatus(RequestStatus requestStatus, int page, int size);

  List<ResignListResponse> getResignListByUserId(Long userId);

  ResignRequest getResignById(Long resignId);

  boolean setResignRequestStatus(Long resignId,
                                 RequestStatus requestStatus);

  ResponseEntity<?> deleteResignationRequestById(UserPrincipal currentUser,
                                                 Long resignId);

  ResponseEntity<?> makeResignRequest(User user, Date date, String reason);

}
