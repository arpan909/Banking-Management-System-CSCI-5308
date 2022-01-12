package asd.group2.bms.repositoryImpl;

import asd.group2.bms.model.user.Role;
import asd.group2.bms.model.user.RoleType;
import asd.group2.bms.repository.IRoleRepository;
import asd.group2.bms.repositoryMapper.RoleRowMapper;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

@Repository
public class RoleRepositoryImpl extends JdbcDaoSupport implements IRoleRepository {

  private final JdbcTemplate jdbcTemplate;

  final
  DataSource dataSource;

  public RoleRepositoryImpl(JdbcTemplate jdbcTemplate, DataSource dataSource) {
    this.jdbcTemplate = jdbcTemplate;
    this.dataSource = dataSource;
  }

  @PostConstruct
  private void initialize() {
    setDataSource(dataSource);
  }

  /**
   * @param roleName: role of the user
   * @return This will return the role of the user
   */
  public Optional<Role> findByName(RoleType roleName) {
    String sql = "SELECT * FROM roles WHERE name = ?";
    try {
      return Optional.ofNullable(jdbcTemplate.queryForObject(sql,
          new Object[]{roleName.name()},
          new RoleRowMapper()));
    } catch (EmptyResultDataAccessException e) {
      return Optional.empty();
    }
  }

  public List<Role> findRoleByUserId(Long userId) {
    String sql = "SELECT r.id, ur.name FROM user_roles ur INNER JOIN roles r " +
        "ON ur.role_id = r.id " +
        "WHERE ur.user_id = ?";
    return jdbcTemplate.query(sql, new Object[]{userId},
        new RoleRowMapper());
  }

}