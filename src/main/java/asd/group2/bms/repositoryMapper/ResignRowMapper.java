package asd.group2.bms.repositoryMapper;

import asd.group2.bms.model.resign.RequestStatus;
import asd.group2.bms.model.resign.ResignRequest;
import asd.group2.bms.model.user.User;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ResignRowMapper implements RowMapper<ResignRequest> {

  @Override
  public ResignRequest mapRow(ResultSet resultSet, int i) throws SQLException {
    ResignRequest resignRequest = new ResignRequest();
    CommonMapping commonMapping = new CommonMapping();
    UserRowMapper userRowMapper = new UserRowMapper();

    resignRequest.setCreatedAt(commonMapping.mapCreatedAtOrUpdatedAt(resultSet.getDate(
        "created_at").toLocalDate()));
    resignRequest.setUpdatedAt(commonMapping.mapCreatedAtOrUpdatedAt(resultSet.getDate(
        "updated_at").toLocalDate()));
    resignRequest.setResignId(resultSet.getLong("resign_id"));
    resignRequest.setDate(resultSet.getDate("date"));
    resignRequest.setReason(resultSet.getString("reason"));
    resignRequest.setRequestStatus(RequestStatus.valueOf(resultSet.getString("request_status")));

    User user = userRowMapper.mapRow(resultSet, i);
    resignRequest.setUser(user);

    return resignRequest;
  }

}
