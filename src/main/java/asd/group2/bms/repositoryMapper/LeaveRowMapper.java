package asd.group2.bms.repositoryMapper;

import asd.group2.bms.model.leaves.LeaveRequest;
import asd.group2.bms.model.leaves.RequestStatus;
import asd.group2.bms.model.user.User;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class LeaveRowMapper implements RowMapper<LeaveRequest> {

  @Override
  public LeaveRequest mapRow(ResultSet resultSet, int i) throws SQLException {
    LeaveRequest leaveRequest = new LeaveRequest();
    CommonMapping commonMapping = new CommonMapping();
    UserRowMapper userRowMapper = new UserRowMapper();

    leaveRequest.setCreatedAt(commonMapping.mapCreatedAtOrUpdatedAt(resultSet.getDate(
        "created_at").toLocalDate()));
    leaveRequest.setUpdatedAt(commonMapping.mapCreatedAtOrUpdatedAt(resultSet.getDate(
        "updated_at").toLocalDate()));
    leaveRequest.setLeaveId(resultSet.getLong("leave_id"));
    leaveRequest.setFromDate(resultSet.getDate("from_date"));
    leaveRequest.setReason(resultSet.getString("reason"));
    leaveRequest.setRequestStatus(RequestStatus.valueOf(resultSet.getString("request_status")));
    leaveRequest.setToDate(resultSet.getDate("to_date"));

    User user = userRowMapper.mapRow(resultSet, i);
    leaveRequest.setUser(user);

    return leaveRequest;
  }

}
