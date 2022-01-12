package asd.group2.bms.repositoryImpl;

import asd.group2.bms.model.resign.RequestStatus;
import asd.group2.bms.model.resign.ResignRequest;
import asd.group2.bms.model.user.User;
import asd.group2.bms.repository.IResignRepository;
import asd.group2.bms.repositoryMapper.ResignRowMapper;
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
public class ResignRepositoryImpl extends JdbcDaoSupport implements IResignRepository {

  private final JdbcTemplate jdbcTemplate;

  final
  DataSource dataSource;

  public ResignRepositoryImpl(JdbcTemplate jdbcTemplate, DataSource dataSource) {
    this.jdbcTemplate = jdbcTemplate;
    this.dataSource = dataSource;
  }

  @PostConstruct
  private void initialize() {
    setDataSource(dataSource);
  }

  @Override
  public Page<ResignRequest> findByRequestStatusEquals(RequestStatus requestStatus, Pageable pageable) {
    String rowCountSql = "SELECT count(1) AS row_count " +
        "FROM resigns " +
        "WHERE request_status = " + "\"" + requestStatus + "\"";

    int total =
        jdbcTemplate.queryForObject(rowCountSql, Integer.class);

    String querySql = "SELECT * FROM resigns r INNER JOIN users u ON r.user_id = u.id INNER JOIN user_roles ur ON u.id = " +
        "ur.user_id INNER JOIN roles ro ON ro.id = ur.role_id WHERE request_status = " + "\"" + requestStatus + "\"" +
        "LIMIT " + pageable.getPageSize() +
        " OFFSET " + pageable.getOffset();

    List<ResignRequest> resignRequests = jdbcTemplate.query(
        querySql, new ResignRowMapper()
    );

    return new PageImpl<>(resignRequests, pageable, total);
  }

  @Override
  public List<ResignRequest> findByUser_Id(Long userId) {
    String sql = "SELECT * FROM resigns r INNER JOIN users u ON r.user_id = u.id INNER JOIN user_roles ur ON u.id = ur.user_id INNER JOIN roles ro ON ro.id = ur.role_id WHERE r.user_id = ?";
    try {
      return jdbcTemplate.query(sql, new Object[]{userId}, new ResignRowMapper());
    } catch (EmptyResultDataAccessException e) {
      return Collections.emptyList();
    }
  }

  @Override
  public Optional<ResignRequest> findById(Long resignId) {
    String sql = "SELECT * FROM resigns r INNER JOIN users u ON r.user_id = u.id INNER JOIN user_roles ur ON u.id = ur.user_id INNER JOIN roles ro ON ro.id = ur.role_id WHERE r.resign_id = ?";
    try {
      return Optional.ofNullable(jdbcTemplate.queryForObject(sql, new Object[]{resignId},
          new ResignRowMapper()));
    } catch (EmptyResultDataAccessException e) {
      return Optional.empty();
    }
  }

  @Override
  public List<ResignRequest> findByUserOrderByCreatedAtDesc(User user) {
    String sql = "SELECT * FROM resigns r INNER JOIN users u ON r.user_id = u.id INNER JOIN user_roles ur ON u.id = ur.user_id INNER JOIN roles ro ON ro.id = ur.role_id WHERE r.user_id = ? ORDER BY r.created_at DESC";
    try {
      return jdbcTemplate.query(sql, new Object[]{user.getId()}, new ResignRowMapper());
    } catch (EmptyResultDataAccessException e) {
      return Collections.emptyList();
    }
  }

  @Override
  public ResignRequest save(ResignRequest resignRequest) {
    Date now = new Date();
    KeyHolder keyHolder = new GeneratedKeyHolder();

    String resignSql = "INSERT INTO resigns (" +
        "created_at, updated_at, date, reason, request_status, user_id)" +
        "VALUES (?, ?, ?, ?, ?, ?)";

    jdbcTemplate.update(
        connection -> {
          PreparedStatement ps =
              connection.prepareStatement(resignSql,
                  Statement.RETURN_GENERATED_KEYS);
          ps.setDate(1, new java.sql.Date(now.getTime()));
          ps.setDate(2, new java.sql.Date(now.getTime()));
          ps.setDate(3, new java.sql.Date(resignRequest.getDate().getTime()));
          ps.setString(4, resignRequest.getReason());
          ps.setString(5, resignRequest.getRequestStatus().name());
          ps.setLong(6, resignRequest.getUser().getId());
          return ps;
        }, keyHolder);

    return resignRequest;
  }

  @Override
  public Boolean update(ResignRequest resignRequest) {
    String sql = "UPDATE resigns SET " +
        "updated_at = ?, date = ?, reason = ?, " +
        "request_status = ? WHERE resign_id = ?";
    int status = jdbcTemplate.update(sql,
        new Date(),
        resignRequest.getDate(), resignRequest.getReason(),
        resignRequest.getRequestStatus().name(), resignRequest.getResignId());

    if (status == 0) {
      return false;
    }

    return true;
  }

  @Override
  public void delete(Long resignId) {
    String sql = "DELETE from resigns WHERE resign_id = ?";
    Object[] args = new Object[]{resignId};
    jdbcTemplate.update(sql, args);
  }

}
