package asd.group2.bms.repositoryMapper;

import asd.group2.bms.model.user.Role;
import asd.group2.bms.model.user.RoleType;
import asd.group2.bms.repositoryImpl.RoleRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class RoleRowMapper implements RowMapper<Role> {

  @Autowired
  RoleRepositoryImpl roleRepository;

  @Override
  public Role mapRow(ResultSet resultSet, int i) throws SQLException {
    Role role = new Role();
    role.setId(resultSet.getLong("id"));
    role.setName(RoleType.valueOf(resultSet.getString("name")));
    return role;
  }

}
