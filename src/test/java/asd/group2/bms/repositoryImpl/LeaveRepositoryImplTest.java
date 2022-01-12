package asd.group2.bms.repositoryImpl;

import asd.group2.bms.model.leaves.LeaveRequest;
import asd.group2.bms.model.leaves.RequestStatus;
import asd.group2.bms.model.user.User;
import asd.group2.bms.repositoryMapper.LeaveRowMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.util.ReflectionTestUtils;

import java.sql.ResultSet;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LeaveRepositoryImplTest {

  @Mock
  private JdbcTemplate jdbcTemplate;

  @InjectMocks
  private LeaveRepositoryImpl leaveRepository;

  @BeforeEach
  void setUp() {

    MockitoAnnotations.initMocks(this);
    ReflectionTestUtils.setField(leaveRepository, "jdbcTemplate", jdbcTemplate);
  }

  @Test
  public void findByRequestStatusEqualsTest() {

    Long leaveId = 1L;
    Integer totalPage = 1;

    when(jdbcTemplate.queryForObject(ArgumentMatchers.anyString(), (Class<Object>) ArgumentMatchers.any())).thenAnswer((invocation) -> {
      ResultSet rs = Mockito.mock(ResultSet.class);
      return totalPage;
    });

    LeaveRequest leaveRequest = new LeaveRequest();
    leaveRequest.setLeaveId(leaveId);

    List<LeaveRequest> leaves = new ArrayList<>();
    leaves.add(leaveRequest);

    when(jdbcTemplate.query(ArgumentMatchers.anyString(), ArgumentMatchers.any(LeaveRowMapper.class))).thenReturn(leaves);

    Pageable pageable = PageRequest.of(0, 1, Sort.Direction.ASC, "createdAt");

    Page<LeaveRequest> requestPage = leaveRepository.findByRequestStatusEquals(RequestStatus.PENDING, pageable);

    assertEquals(totalPage, requestPage.getTotalPages());
  }

  @Test
  public void findByUser_IdTestSuccess() {

    Long leaveId = 1L;

    User user = new User();
    user.setUsername("shivam");
    user.setId(leaveId);

    LeaveRequest leaveRequest = new LeaveRequest();
    leaveRequest.setUser(user);
    leaveRequest.setReason("reason 1");

    List<LeaveRequest> leaves = new ArrayList<>();
    leaves.add(leaveRequest);

    when(jdbcTemplate.query(ArgumentMatchers.anyString(), (Object[]) ArgumentMatchers.any(), ArgumentMatchers.any(LeaveRowMapper.class))).thenReturn(leaves);

    List<LeaveRequest> leave = leaveRepository.findByUser_Id(1L);
    assertEquals("reason 1", leave.get(0).getReason());
  }

  @Test
  public void findByUser_IdTestFail() {

    Long leaveId = 1L;

    User user = new User();
    user.setUsername("shivam");
    user.setId(leaveId);

    LeaveRequest leaveRequest = new LeaveRequest();
    leaveRequest.setUser(user);

    List<LeaveRequest> leaves = new ArrayList<>();
    leaves.add(leaveRequest);

    when(jdbcTemplate.query(ArgumentMatchers.anyString(), (Object[]) ArgumentMatchers.any(), ArgumentMatchers.any(LeaveRowMapper.class))).thenThrow(new EmptyResultDataAccessException(1));

    List<LeaveRequest> leave = leaveRepository.findByUser_Id(1L);

    assertEquals(0, leave.size());
  }

  @Test
  public void findByIdTestSuccess() {

    Long leaveId = 1L;

    User user = new User();
    user.setUsername("shivam");
    user.setId(leaveId);

    LeaveRequest leaveRequest = new LeaveRequest();
    leaveRequest.setUser(user);
    leaveRequest.setReason("reason 1");

    when(jdbcTemplate.queryForObject(ArgumentMatchers.anyString(), (Object[]) ArgumentMatchers.any(), ArgumentMatchers.any(LeaveRowMapper.class))).thenReturn(leaveRequest);

    Optional<LeaveRequest> leave = leaveRepository.findById(1L);

    assertEquals("reason 1", leave.get().getReason());
  }

  @Test
  public void findByIdTestFail() {

    Long leaveId = 1L;

    User user = new User();
    user.setUsername("shivam");
    user.setId(leaveId);

    LeaveRequest leaveRequest = new LeaveRequest();
    leaveRequest.setUser(user);

    when(jdbcTemplate.queryForObject(ArgumentMatchers.anyString(), (Object[]) ArgumentMatchers.any(), ArgumentMatchers.any(LeaveRowMapper.class))).thenThrow(new EmptyResultDataAccessException(1));

    Optional<LeaveRequest> leave = leaveRepository.findById(1L);

    assertEquals(false, leave.isPresent());
  }

  @Test
  public void updateTest() throws ParseException {

    User user = new User();
    long leaveId = 1L;
    user.setUsername("shivam");
    String fromDateStr = "2021/07/20";
    Date fromDate = new SimpleDateFormat("yyyy/MM/dd").parse(fromDateStr);
    String toDateStr = "2021/07/25";
    Date toDate = new SimpleDateFormat("yyyy/MM/dd").parse(toDateStr);
    LeaveRequest leaveRequest = new LeaveRequest();
    leaveRequest.setLeaveId(leaveId);
    leaveRequest.setRequestStatus(asd.group2.bms.model.leaves.RequestStatus.PENDING);
    leaveRequest.setFromDate(fromDate);
    leaveRequest.setToDate(toDate);
    leaveRequest.setReason("reason");
    leaveRequest.setUser(user);

    when(jdbcTemplate.update(ArgumentMatchers.any(), (Object[]) ArgumentMatchers.any(), (Object[]) ArgumentMatchers.any(), (Object[]) ArgumentMatchers.any(), (Object[]) ArgumentMatchers.any(), (Object[]) ArgumentMatchers.any(), (Object[]) ArgumentMatchers.any())).thenReturn(1);

    Boolean response = leaveRepository.update(leaveRequest);
    assertEquals(true, response);

  }

  @Test
  public void deleteTest() {

    Long leaveId = 1L;

    when(jdbcTemplate.update(ArgumentMatchers.any(), (Object[]) ArgumentMatchers.any())).thenReturn(1);

    leaveRepository.delete(leaveId);

    verify(jdbcTemplate, times(1)).update(anyString(), (Object) any());
  }

}
