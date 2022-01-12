package asd.group2.bms.repositoryMapper;

import asd.group2.bms.model.account.AccountType;
import asd.group2.bms.model.user.AccountStatus;
import asd.group2.bms.model.user.Role;
import asd.group2.bms.model.user.RoleType;
import asd.group2.bms.model.user.User;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class UserRowMapper implements RowMapper<User> {

  @Override
  public User mapRow(ResultSet resultSet, int i) throws SQLException {
    CommonMapping commonMapping = new CommonMapping();
    User user = new User();
    Role role = new Role();

    user.setCreatedAt(commonMapping.mapCreatedAtOrUpdatedAt(resultSet.getDate(
        "created_at").toLocalDate()));
    user.setUpdatedAt(commonMapping.mapCreatedAtOrUpdatedAt(resultSet.getDate(
        "updated_at").toLocalDate()));
    user.setId(resultSet.getLong("id"));
    user.setFirstName(resultSet.getString("first_name"));
    user.setLastName(resultSet.getString("last_name"));
    user.setAccountStatus(AccountStatus.values()[resultSet.getInt("account_status")]);
    user.setAddress(resultSet.getString("address"));
    user.setBirthday(resultSet.getDate("birthday"));
    user.setEmail(resultSet.getString("email"));
    user.setPassword(resultSet.getString("password"));
    user.setPhone(resultSet.getString("phone"));
    user.setUsername(resultSet.getString("username"));
    user.setForgotPasswordToken(resultSet.getString("forgot_password_token"));
    user.setRequestedAccountType(AccountType.valueOf(resultSet.getString(
        "requested_account_type")));
    user.setCity(resultSet.getString("city"));
    user.setState(resultSet.getString("state"));
    user.setZipCode(resultSet.getString("zip_code"));
    role.setName(RoleType.valueOf(resultSet.getString("name")));
    role.setId(resultSet.getLong("role_id"));
    user.getRoles().add(role);
    user.setRoles(user.getRoles());

    return user;
  }

}
