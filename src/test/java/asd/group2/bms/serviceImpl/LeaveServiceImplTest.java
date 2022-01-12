package asd.group2.bms.serviceImpl;

import asd.group2.bms.model.leaves.LeaveRequest;
import asd.group2.bms.model.leaves.RequestStatus;
import asd.group2.bms.model.user.User;
import asd.group2.bms.payload.response.LeaveListResponse;
import asd.group2.bms.payload.response.PagedResponse;
import asd.group2.bms.repository.ILeaveRepository;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LeaveServiceImplTest {

  @Mock
  ILeaveRepository leaveRepository;

  @InjectMocks
  LeaveServiceImpl leaveService;

  @BeforeEach
  void setUp() {
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
  void getLeavesByStatusTest() {
    String username = "shivam";
    asd.group2.bms.model.leaves.RequestStatus requestStatus = RequestStatus.PENDING;
    int page = 0;
    int size = 3;

    Pageable pageable = PageRequest.of(page, size, Sort.Direction.ASC, "createdAt");

    User user = new User();
    user.setUsername("shivam");

    asd.group2.bms.model.leaves.LeaveRequest leaveRequest = new asd.group2.bms.model.leaves.LeaveRequest();
    leaveRequest.setUser(user);

    List<LeaveRequest> leaves = new ArrayList<>();
    leaves.add(leaveRequest);

    Page<LeaveRequest> pagedLeaves = new PageImpl<>(leaves);

    when(leaveRepository.findByRequestStatusEquals(requestStatus, pageable)).thenReturn(pagedLeaves);

    PagedResponse<LeaveListResponse> leavesList =
        leaveService.getLeavesByStatus(requestStatus, page, size);

    assertEquals(username,
        leavesList.getContent().get(0).getUserMetaResponse().getUsername());
  }

  @Test
  void getLeavesByStatusTestEmpty() {

    RequestStatus requestStatus = RequestStatus.PENDING;
    int page = 0;
    int size = 3;

    Pageable pageable = PageRequest.of(page, size, Sort.Direction.ASC, "createdAt");

    List<LeaveRequest> leaves = new ArrayList<>();

    Page<LeaveRequest> pagedLeaves = new PageImpl<>(leaves);

    when(leaveRepository.findByRequestStatusEquals(requestStatus, pageable)).thenReturn(pagedLeaves);

    PagedResponse<LeaveListResponse> resignations =
        leaveService.getLeavesByStatus(requestStatus, page, size);

    assertEquals(0,
        resignations.getSize());
  }

  @Test
  void getLeaveListByUserIdTest() {
    String username = "shivam";
    Long userId = 1L;
    User user = new User();
    user.setUsername("shivam");
    user.setId(userId);

    LeaveRequest leaveRequest = new LeaveRequest();
    leaveRequest.setUser(user);

    List<LeaveRequest> leaves = new ArrayList<>();
    leaves.add(leaveRequest);

    when(leaveRepository.findByUser_Id(user.getId())).thenReturn(leaves);

    List<LeaveListResponse> leaveLists = leaveService.getLeaveListByUserId(user.getId());

    assertEquals(username, leaveLists.get(0).getUserMetaResponse().getUsername());
    assertEquals(1, leaveLists.size());
  }

  @Test
  void getLeaveByIdTest() {
    Long leaveId = 1L;

    User user = new User();
    LeaveRequest leaveRequest = new LeaveRequest();
    leaveRequest.setUser(user);

    when(leaveRepository.findById(leaveId)).thenReturn(Optional.of(leaveRequest));

    leaveService.getLeaveById(leaveId);
    verify(leaveRepository, times(1)).findById(any());
  }

  @Test
  void setLeaveRequestStatusTest() {
    User user = new User();
    Long leaveId = 1L;

    LeaveRequest leaveRequest = new LeaveRequest();
    leaveRequest.setUser(user);

    Optional<LeaveRequest> request = Optional.of(leaveRequest);
    when(leaveRepository.findById(leaveId)).thenReturn(request);

    leaveService.setLeaveRequestStatus(leaveId, asd.group2.bms.model.leaves.RequestStatus.PENDING);
    assertEquals(asd.group2.bms.model.leaves.RequestStatus.PENDING, leaveRequest.getRequestStatus());
    verify(leaveRepository, times(1)).findById(any());
    verify(leaveRepository, times(1)).update(any());
  }

  @Test
  void deleteLeaveRequestByIdSuccessTest() {

    Long userId = 1L;
    Long leaveId = 2L;

    User user = new User();
    user.setId(userId);
    LeaveRequest leaveRequest = new LeaveRequest();
    leaveRequest.setUser(user);
    leaveRequest.setLeaveId(leaveId);
    UserPrincipal userPrincipal = new UserPrincipal();
    userPrincipal.setId(userId);

    Optional<LeaveRequest> request = Optional.of(leaveRequest);

    when(leaveRepository.findById(leaveId)).thenReturn(request);
    doNothing().when(leaveRepository).delete(leaveId);

    leaveService.deleteLeaveRequestById(userPrincipal, leaveId);
    verify(leaveRepository, times(1)).delete(any());
  }

  @Test
  void deleteLeaveRequestByIdFailNotAuthorisedTest() {

    Long userId = 1L;
    Long leaveId = 2L;
    Long secondUserId = 3L;

    User user = new User();
    user.setId(userId);
    LeaveRequest leaveRequest = new LeaveRequest();
    leaveRequest.setUser(user);
    leaveRequest.setLeaveId(leaveId);
    UserPrincipal userPrincipal = new UserPrincipal();
    userPrincipal.setId(secondUserId);

    Optional<LeaveRequest> request = Optional.of(leaveRequest);

    when(leaveRepository.findById(leaveId)).thenReturn(request);

    leaveService.deleteLeaveRequestById(userPrincipal, leaveId);
    verify(leaveRepository, times(0)).delete(any());
  }

  @Test
  void deleteLeaveRequestByIdFailExceptionTest() {

    Long userId = 1L;
    Long leaveId = 2L;

    User user = new User();
    user.setId(userId);
    LeaveRequest leaveRequest = new LeaveRequest();
    leaveRequest.setLeaveId(leaveId);
    UserPrincipal userPrincipal = new UserPrincipal();
    userPrincipal.setId(userId);

    when(leaveRepository.findById(leaveId)).thenThrow(new RuntimeException());
    ResponseEntity<?> responseEntity =
        leaveService.deleteLeaveRequestById(userPrincipal, leaveId);

    assertEquals(HttpStatus.BAD_REQUEST.toString(),
        responseEntity.getStatusCode().toString());
  }

  @Test
  void makeLeaveRequestNewTest() throws ParseException {

    Long leaveId = 1L;
    String fromDateStr = "2021/07/20";
    Date fromDate = new SimpleDateFormat("yyyy/MM/dd").parse(fromDateStr);
    String toDateStr = "2021/07/25";
    Date toDate = new SimpleDateFormat("yyyy/MM/dd").parse(toDateStr);

    User user = new User();
    String reason = "reason";
    LeaveRequest leaveRequest = new LeaveRequest();
    leaveRequest.setLeaveId(leaveId);
    leaveRequest.setUser(user);
    leaveRequest.setFromDate(fromDate);
    leaveRequest.setToDate(toDate);
    leaveRequest.setReason(reason);
    leaveRequest.setRequestStatus(RequestStatus.PENDING);

    when(leaveRepository.save(any())).thenReturn(leaveRequest);

    ResponseEntity<?> leave = leaveService.makeLeaveRequest(user, fromDate, toDate, reason);

    assertEquals(HttpStatus.OK, leave.getStatusCode());
    verify(leaveRepository, times(1)).save(any());
  }

  @Test
  void makeLeaveRequestExceptionTest() throws ParseException {

    Long userId = 1L;
    User user = new User();
    user.setId(userId);
    String fromDateStr = "2021/07/20";
    Date fromDate = new SimpleDateFormat("yyyy/MM/dd").parse(fromDateStr);
    String toDateStr = "2021/07/25";
    Date toDate = new SimpleDateFormat("yyyy/MM/dd").parse(toDateStr);
    String reason = "reason";
    LeaveRequest leaveRequest = new LeaveRequest();

    List<LeaveRequest> leaves = new ArrayList<>();

    when(leaveRepository.findByUser_Id(userId)).thenReturn(leaves);

    when(leaveRepository.save(leaveRequest)).thenReturn(leaveRequest);

    ResponseEntity<?> leave = leaveService.makeLeaveRequest(user, fromDate, toDate, reason);

    assertEquals(HttpStatus.BAD_REQUEST, leave.getStatusCode());
  }

  @Test
  void makeLeaveRequestPendingTest() throws ParseException {
    Long userId = 1L;
    User user = new User();
    user.setId(userId);
    String fromDateStr = "2021/07/20";
    Date fromDate = new SimpleDateFormat("yyyy/MM/dd").parse(fromDateStr);
    String toDateStr = "2021/07/25";
    Date toDate = new SimpleDateFormat("yyyy/MM/dd").parse(toDateStr);
    String reason = "reason";
    LeaveRequest leaveRequest = new LeaveRequest();
    leaveRequest.setUser(user);
    leaveRequest.setFromDate(fromDate);
    leaveRequest.setToDate(toDate);
    leaveRequest.setReason(reason);
    leaveRequest.setRequestStatus(RequestStatus.PENDING);

    List<LeaveRequest> leaves = new ArrayList<>();
    leaves.add(leaveRequest);

    when(leaveRepository.findByUser_Id(userId)).thenReturn(leaves);

    ResponseEntity<?> leave = leaveService.makeLeaveRequest(user, fromDate, toDate, reason);

    assertEquals(HttpStatus.NOT_ACCEPTABLE, leave.getStatusCode());
  }

  @Test
  void makeLeaveRequestApprovedTest() throws ParseException {
    Long userId = 1L;
    User user = new User();
    user.setId(userId);
    String fromDateStr = "2021/07/20";
    Date fromDate = new SimpleDateFormat("yyyy/MM/dd").parse(fromDateStr);
    String toDateStr = "2021/07/25";
    Date toDate = new SimpleDateFormat("yyyy/MM/dd").parse(toDateStr);
    String reason = "reason";
    LeaveRequest leaveRequest = new LeaveRequest();
    leaveRequest.setUser(user);
    leaveRequest.setFromDate(fromDate);
    leaveRequest.setToDate(toDate);
    leaveRequest.setReason(reason);
    leaveRequest.setRequestStatus(RequestStatus.APPROVED);

    List<LeaveRequest> leaves = new ArrayList<>();
    leaves.add(leaveRequest);

    when(leaveRepository.findByUser_Id(userId)).thenReturn(leaves);

    ResponseEntity<?> leave = leaveService.makeLeaveRequest(user, fromDate, toDate, reason);

    assertEquals(HttpStatus.NOT_ACCEPTABLE, leave.getStatusCode());
  }

}