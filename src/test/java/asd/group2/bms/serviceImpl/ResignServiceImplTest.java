package asd.group2.bms.serviceImpl;

import asd.group2.bms.model.resign.RequestStatus;
import asd.group2.bms.model.resign.ResignRequest;
import asd.group2.bms.model.user.User;
import asd.group2.bms.payload.response.PagedResponse;
import asd.group2.bms.payload.response.ResignListResponse;
import asd.group2.bms.repository.IResignRepository;
import asd.group2.bms.security.UserPrincipal;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ResignServiceImplTest {

  @Mock
  IResignRepository resignRepository;

  @InjectMocks
  ResignServiceImpl resignService;

  @BeforeEach
  public void setup() {
    HttpServletRequest mockRequest = new MockHttpServletRequest();
    ServletRequestAttributes servletRequestAttributes = new ServletRequestAttributes(mockRequest);
    RequestContextHolder.setRequestAttributes(servletRequestAttributes);
    MockitoAnnotations.openMocks(this);
  }

  @AfterEach
  public void teardown() {
    RequestContextHolder.resetRequestAttributes();
  }

  @Test
  void getResignListByStatusTest() {
    String username = "aditya";
    RequestStatus requestStatus = RequestStatus.PENDING;
    int page = 0;
    int size = 3;

    Pageable pageable = PageRequest.of(page, size, Sort.Direction.ASC, "createdAt");

    User user = new User();
    user.setUsername("aditya");

    ResignRequest resignRequest = new ResignRequest();
    resignRequest.setUser(user);

    List<ResignRequest> resigns = new ArrayList<>();
    resigns.add(resignRequest);

    Page<ResignRequest> pagedResigns = new PageImpl<>(resigns);

    when(resignRepository.findByRequestStatusEquals(requestStatus, pageable)).thenReturn(pagedResigns);

    PagedResponse<ResignListResponse> resignations =
        resignService.getResignListByStatus(requestStatus, page, size);

    assertEquals(username,
        resignations.getContent().get(0).getUserMetaResponse().getUsername());
  }

  @Test
  void getResignListByStatusTestEmpty() {
    RequestStatus requestStatus = RequestStatus.PENDING;
    int page = 0;
    int size = 3;

    Pageable pageable = PageRequest.of(page, size, Sort.Direction.ASC, "createdAt");

    List<ResignRequest> resigns = new ArrayList<>();

    Page<ResignRequest> pagedResigns = new PageImpl<>(resigns);

    when(resignRepository.findByRequestStatusEquals(requestStatus, pageable)).thenReturn(pagedResigns);

    PagedResponse<ResignListResponse> resignations =
        resignService.getResignListByStatus(requestStatus, page, size);

    assertEquals(0,
        resignations.getSize());
  }

  @Test
  void getResignListByUserIdTest() {
    String username = "aditya";

    User user = new User();
    user.setUsername("aditya");
    user.setId(1L);

    ResignRequest resignRequest = new ResignRequest();
    resignRequest.setUser(user);

    List<ResignRequest> resigns = new ArrayList<>();
    resigns.add(resignRequest);

    when(resignRepository.findByUser_Id(user.getId())).thenReturn(resigns);

    List<ResignListResponse> resign = resignService.getResignListByUserId(user.getId());

    assertEquals(username, resign.get(0).getUserMetaResponse().getUsername());
    assertEquals(1, resign.size());
  }

  @Test
  void getResignByIdTest() {
    Long resignId = 1L;
    when(resignRepository.findById(resignId)).thenReturn(Optional.of(new ResignRequest()));

    resignService.getResignById(resignId);
    verify(resignRepository, times(1)).findById(any());
  }

  @Test()
  void setResignRequestStatusTest() {
    User user = new User();

    ResignRequest resignRequest = new ResignRequest();
    resignRequest.setUser(user);

    Optional<ResignRequest> request = Optional.of(resignRequest);
    when(resignRepository.findById(1L)).thenReturn(request);

    resignService.setResignRequestStatus(1L, RequestStatus.PENDING);
    assertEquals(RequestStatus.PENDING, resignRequest.getRequestStatus());
    verify(resignRepository, times(1)).findById(any());
    verify(resignRepository, times(1)).update(any());
  }

  @Test
  void deleteResignationRequestByIdTestSuccess() {
    User user = new User();
    user.setId(1L);
    ResignRequest resignRequest = new ResignRequest();
    resignRequest.setUser(user);
    resignRequest.setResignId(2L);
    UserPrincipal userPrincipal = new UserPrincipal();
    userPrincipal.setId(1L);

    Optional<ResignRequest> request = Optional.of(resignRequest);

    Long resignId = 2L;

    when(resignRepository.findById(resignId)).thenReturn(request);
    doNothing().when(resignRepository).delete(resignId);

    resignService.deleteResignationRequestById(userPrincipal, resignId);
    verify(resignRepository, times(1)).delete(any());
  }

  @Test
  void deleteResignationRequestByIdTestFailNotAuthorised() {
    User user = new User();
    user.setId(1L);
    ResignRequest resignRequest = new ResignRequest();
    resignRequest.setUser(user);
    resignRequest.setResignId(2L);
    UserPrincipal userPrincipal = new UserPrincipal();
    userPrincipal.setId(3L);

    Long resignId = 2L;

    Optional<ResignRequest> request = Optional.of(resignRequest);

    when(resignRepository.findById(resignId)).thenReturn(request);

    resignService.deleteResignationRequestById(userPrincipal, resignId);
    verify(resignRepository, times(0)).delete(any());
  }

  @Test
  void deleteResignationRequestByIdTestFailException() {
    User user = new User();
    user.setId(1L);
    ResignRequest resignRequest = new ResignRequest();
    resignRequest.setResignId(2L);
    UserPrincipal userPrincipal = new UserPrincipal();
    userPrincipal.setId(1L);

    Long resignId = 2L;

    when(resignRepository.findById(resignId)).thenThrow(new RuntimeException());
    ResponseEntity<?> responseEntity =
        resignService.deleteResignationRequestById(userPrincipal, resignId);

    assertEquals(HttpStatus.BAD_REQUEST.toString(),
        responseEntity.getStatusCode().toString());
  }

  @Test
  void makeResignRequestTestNew() {
    User user = new User();
    Date date = new Date();
    String reason = "reason";
    ResignRequest resignRequest = new ResignRequest();
    resignRequest.setResignId(1L);
    resignRequest.setUser(user);
    resignRequest.setDate(date);
    resignRequest.setReason(reason);
    resignRequest.setRequestStatus(RequestStatus.PENDING);
    List<ResignRequest> resigns = new ArrayList<>();

    when(resignRepository.findByUserOrderByCreatedAtDesc(user)).thenReturn(resigns);
    when(resignRepository.save(any())).thenReturn(resignRequest);

    ResponseEntity<?> resign = resignService.makeResignRequest(user, date, reason);

    assertEquals(HttpStatus.OK, resign.getStatusCode());
    verify(resignRepository, times(1)).save(any());
  }

  @Test
  void makeResignRequestTestException() {
    User user = new User();
    Date date = new Date();
    String reason = "reason";
    ResignRequest resignRequest = new ResignRequest();

    List<ResignRequest> resigns = new ArrayList<>();

    when(resignRepository.findByUserOrderByCreatedAtDesc(user)).thenReturn(resigns);
    when(resignRepository.save(resignRequest)).thenReturn(resignRequest);

    ResponseEntity<?> resign = resignService.makeResignRequest(user, date, reason);

    assertEquals(HttpStatus.BAD_REQUEST, resign.getStatusCode());
  }

  @Test
  void makeResignRequestTestPENDING() {
    User user = new User();
    Date date = new Date();
    String reason = "reason";
    ResignRequest resignRequest = new ResignRequest();
    resignRequest.setUser(user);
    resignRequest.setRequestStatus(RequestStatus.PENDING);

    List<ResignRequest> resigns = new ArrayList<>();
    resigns.add(resignRequest);

    when(resignRepository.findByUserOrderByCreatedAtDesc(user)).thenReturn(resigns);

    ResponseEntity<?> resign = resignService.makeResignRequest(user, date, reason);

    assertEquals(HttpStatus.NOT_ACCEPTABLE, resign.getStatusCode());
  }

  @Test
  void makeResignRequestTestAPPROVED() {
    User user = new User();
    Date date = new Date();
    String reason = "reason";
    ResignRequest resignRequest = new ResignRequest();
    resignRequest.setUser(user);
    resignRequest.setRequestStatus(RequestStatus.APPROVED);

    List<ResignRequest> resigns = new ArrayList<>();
    resigns.add(resignRequest);

    when(resignRepository.findByUserOrderByCreatedAtDesc(user)).thenReturn(resigns);

    ResponseEntity<?> resign = resignService.makeResignRequest(user, date, reason);

    assertEquals(HttpStatus.NOT_ACCEPTABLE, resign.getStatusCode());
  }

}
