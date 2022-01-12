package asd.group2.bms.repositoryImpl;

import asd.group2.bms.model.leaves.LeaveRequest;
import asd.group2.bms.model.leaves.RequestStatus;
import asd.group2.bms.repository.ILeaveRepository;
import asd.group2.bms.repositoryMapper.LeaveRowMapper;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public class LeaveRepositoryImpl extends JdbcDaoSupport implements ILeaveRepository {

  private final JdbcTemplate jdbcTemplate;

  final
  DataSource dataSource;

  public LeaveRepositoryImpl(JdbcTemplate jdbcTemplate, DataSource dataSource) {
    this.jdbcTemplate = jdbcTemplate;
    this.dataSource = dataSource;
  }

  @PostConstruct
  private void initialize() {
    setDataSource(dataSource);
  }

  @Override
  public Page<LeaveRequest> findByRequestStatusEquals(RequestStatus requestStatus, Pageable pageable) {
    String rowCountSql = "SELECT count(1) AS row_count " +
        "FROM leaves " +
        "WHERE request_status = " + "\"" + requestStatus + "\"";

    int total =
        jdbcTemplate.queryForObject(rowCountSql, Integer.class);

    String querySql = "SELECT * FROM leaves l INNER JOIN users u ON l.user_id" +
        " = u.id INNER JOIN user_roles ur ON u.id = " +
        "ur.user_id INNER JOIN roles ro ON ro.id = ur.role_id WHERE request_status = " + "\"" + requestStatus + "\"" +
        "LIMIT " + pageable.getPageSize() +
        " OFFSET " + pageable.getOffset();

    List<LeaveRequest> leaveRequests = jdbcTemplate.query(
        querySql, new LeaveRowMapper()
    );

    return new PageImpl<>(leaveRequests, pageable, total);
  }

  @Override
  public List<LeaveRequest> findByUser_Id(Long userId) {
    String sql = "SELECT * FROM leaves l INNER JOIN users u ON l.user_id = u" +
        ".id INNER JOIN user_roles ur ON u.id = ur.user_id INNER JOIN roles " +
        "ro ON ro.id = ur.role_id WHERE l.user_id = ?";
    try {
      return jdbcTemplate.query(sql, new Object[]{userId},
          new LeaveRowMapper());
    } catch (EmptyResultDataAccessException e) {
      return Collections.emptyList();
    }
  }

  @Override
  public Optional<LeaveRequest> findById(Long leaveId) {
    String sql = "SELECT * FROM leaves l INNER JOIN users u ON l.user_id = u" +
        ".id INNER JOIN user_roles ur ON u.id = ur.user_id INNER JOIN roles " +
        "ro ON ro.id = ur.role_id WHERE l.leave_id = ?";
    try {
      return Optional.ofNullable(jdbcTemplate.queryForObject(sql, new Object[]{leaveId},
          new LeaveRowMapper()));
    } catch (EmptyResultDataAccessException e) {
      return Optional.empty();
    }
  }

  @Override
  public LeaveRequest save(LeaveRequest leaveRequest) {
    Date now = new Date();
    KeyHolder keyHolder = new GeneratedKeyHolder();

    String leaveSql = "INSERT INTO leaves (" +
        "created_at, updated_at, from_date, reason, request_status, to_date, " +
        "user_id)" +
        "VALUES (?, ?, ?, ?, ?, ?, ?)";

    jdbcTemplate.update(
        connection -> {
          PreparedStatement ps =
              connection.prepareStatement(leaveSql,
                  Statement.RETURN_GENERATED_KEYS);
          ps.setDate(1, new java.sql.Date(now.getTime()));
          ps.setDate(2, new java.sql.Date(now.getTime()));
          ps.setDate(3, new java.sql.Date(leaveRequest.getFromDate().getTime()));
          ps.setString(4, leaveRequest.getReason());
          ps.setString(5, leaveRequest.getRequestStatus().name());
          ps.setDate(6, new java.sql.Date(leaveRequest.getToDate().getTime()));
          ps.setLong(7, leaveRequest.getUser().getId());
          return ps;
        }, keyHolder);

    return leaveRequest;
  }

  @Override
  public Boolean update(LeaveRequest leaveRequest) {
    String sql = "UPDATE leaves SET " +
        "updated_at = ?, from_date = ?, reason = ?, " +
        "request_status = ?, to_date = ? WHERE leave_id = ?";
    int status = jdbcTemplate.update(sql,
        new Date(),
        leaveRequest.getFromDate(), leaveRequest.getReason(),
        leaveRequest.getRequestStatus().name(),
        leaveRequest.getToDate(), leaveRequest.getLeaveId());

    if (status == 0) {
      return false;
    }

    return true;
  }

  @Override
  public void delete(Long leaveId) {
    String sql = "DELETE from leaves WHERE leave_id = ?";
    Object[] args = new Object[]{leaveId};
    jdbcTemplate.update(sql, args);
  }

}