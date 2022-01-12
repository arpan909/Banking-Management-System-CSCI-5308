package asd.group2.bms.repositoryImpl;

import asd.group2.bms.model.resign.RequestStatus;
import asd.group2.bms.model.resign.ResignRequest;
import asd.group2.bms.model.user.User;
import asd.group2.bms.repositoryMapper.ResignRowMapper;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ResignRepositoryImplTest {

  @Mock
  private JdbcTemplate jdbcTemplate;

  @InjectMocks
  private ResignRepositoryImpl resignRepository;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.initMocks(this);
    ReflectionTestUtils.setField(resignRepository, "jdbcTemplate", jdbcTemplate);
  }

  @Test
  public void findByRequestStatusEqualsTest() {
    Long resignId = 1L;
    Integer totalPage = 1;

    when(jdbcTemplate.queryForObject(ArgumentMatchers.anyString(),
        (Class<Object>) ArgumentMatchers.any())).thenAnswer((invocation) -> {
      ResultSet rs = Mockito.mock(ResultSet.class);
      return totalPage;
    });

    ResignRequest resignRequest = new ResignRequest();
    resignRequest.setResignId(resignId);

    List<ResignRequest> resigns = new ArrayList<>();
    resigns.add(resignRequest);

    when(jdbcTemplate.query(
        ArgumentMatchers.anyString(),
        ArgumentMatchers.any(ResignRowMapper.class)))
        .thenReturn(resigns);

    Pageable pageable = PageRequest.of(0, 1, Sort.Direction.ASC,
        "createdAt");

    Page<ResignRequest> requestPage =
        resignRepository.findByRequestStatusEquals(RequestStatus.PENDING,
            pageable);

    assertEquals(totalPage, requestPage.getTotalPages());
  }

  @Test
  public void findByUser_IdTestSuccess() {
    User user = new User();
    user.setUsername("aditya");
    user.setId(1L);

    ResignRequest resignRequest = new ResignRequest();
    resignRequest.setUser(user);
    resignRequest.setReason("reason 1");

    List<ResignRequest> resigns = new ArrayList<>();
    resigns.add(resignRequest);

    when(jdbcTemplate.query(
        ArgumentMatchers.anyString(),
        (Object[]) ArgumentMatchers.any(),
        ArgumentMatchers.any(ResignRowMapper.class)))
        .thenReturn(resigns);

    List<ResignRequest> resign = resignRepository.findByUser_Id(1L);
    assertEquals("reason 1", resign.get(0).getReason());
  }

  @Test
  public void findByUser_IdTestFail() {
    User user = new User();
    user.setUsername("aditya");
    user.setId(1L);

    ResignRequest resignRequest = new ResignRequest();
    resignRequest.setUser(user);

    List<ResignRequest> resigns = new ArrayList<>();
    resigns.add(resignRequest);

    when(jdbcTemplate.query(
        ArgumentMatchers.anyString(),
        (Object[]) ArgumentMatchers.any(),
        ArgumentMatchers.any(ResignRowMapper.class)))
        .thenThrow(new EmptyResultDataAccessException(1));

    List<ResignRequest> resign = resignRepository.findByUser_Id(1L);

    assertEquals(0, resign.size());
  }

  @Test
  public void findByIdTestSuccess() {
    User user = new User();
    user.setUsername("aditya");
    user.setId(1L);

    ResignRequest resignRequest = new ResignRequest();
    resignRequest.setUser(user);
    resignRequest.setReason("reason 1");

    when(jdbcTemplate.queryForObject(
        ArgumentMatchers.anyString(),
        (Object[]) ArgumentMatchers.any(),
        ArgumentMatchers.any(ResignRowMapper.class)))
        .thenReturn(resignRequest);

    Optional<ResignRequest> resign = resignRepository.findById(1L);

    assertEquals("reason 1", resign.get().getReason());
  }

  @Test
  public void findByIdTestFail() {
    User user = new User();
    user.setUsername("aditya");
    user.setId(1L);

    ResignRequest resignRequest = new ResignRequest();
    resignRequest.setUser(user);

    when(jdbcTemplate.queryForObject(
        ArgumentMatchers.anyString(),
        (Object[]) ArgumentMatchers.any(),
        ArgumentMatchers.any(ResignRowMapper.class)))
        .thenThrow(new EmptyResultDataAccessException(1));

    Optional<ResignRequest> resign = resignRepository.findById(1L);

    assertEquals(false,
        resign.isPresent());
  }

  @Test
  public void findByUserOrderByCreatedAtDescTestSuccess() {
    User user = new User();
    user.setUsername("aditya");
    user.setId(1L);

    ResignRequest resignRequest = new ResignRequest();
    resignRequest.setUser(user);
    resignRequest.setReason("reason 1");

    List<ResignRequest> resigns = new ArrayList<>();
    resigns.add(resignRequest);

    when(jdbcTemplate.query(
        ArgumentMatchers.anyString(),
        (Object[]) ArgumentMatchers.any(),
        ArgumentMatchers.any(ResignRowMapper.class)))
        .thenReturn(resigns);

    List<ResignRequest> resign =
        resignRepository.findByUserOrderByCreatedAtDesc(user);
    assertEquals("reason 1", resign.get(0).getReason());
  }

  @Test
  public void findByUserOrderByCreatedAtDescTestSuccessFail() {
    User user = new User();
    user.setUsername("aditya");
    user.setId(1L);

    ResignRequest resignRequest = new ResignRequest();
    resignRequest.setUser(user);

    List<ResignRequest> resigns = new ArrayList<>();
    resigns.add(resignRequest);

    when(jdbcTemplate.query(
        ArgumentMatchers.anyString(),
        (Object[]) ArgumentMatchers.any(),
        ArgumentMatchers.any(ResignRowMapper.class)))
        .thenThrow(new EmptyResultDataAccessException(1));

    List<ResignRequest> resign =
        resignRepository.findByUserOrderByCreatedAtDesc(user);

    assertEquals(0, resign.size());
  }


  @Test
  public void updateTest() {
    User user = new User();
    user.setUsername("aditya");
    ResignRequest resignRequest = new ResignRequest();
    resignRequest.setResignId(1L);
    resignRequest.setRequestStatus(RequestStatus.PENDING);
    resignRequest.setDate(new Date());
    resignRequest.setReason("reason");
    resignRequest.setUser(user);

    when(jdbcTemplate.update(ArgumentMatchers.any(),
        (Object[]) ArgumentMatchers.any(), (Object[]) ArgumentMatchers.any(),
        (Object[]) ArgumentMatchers.any(), (Object[]) ArgumentMatchers.any(), (Object[]) ArgumentMatchers.any()
    )).thenReturn(1);

    Boolean response = resignRepository.update(resignRequest);
    assertEquals(true, response);

  }

  @Test
  public void deleteTest() {
    Long resignId = 1L;

    when(jdbcTemplate.update(ArgumentMatchers.any(),
        (Object[]) ArgumentMatchers.any())).thenReturn(1);

    resignRepository.delete(resignId);

    verify(jdbcTemplate, times(1)).update(anyString(), (Object) any());
  }

}
