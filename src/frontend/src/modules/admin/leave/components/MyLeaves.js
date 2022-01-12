import { useContext, useEffect, useState } from "react";
import moment from "moment";

//! Ant Imports

import Card from "antd/lib/card";
import Table from "antd/lib/table";
import Button from "antd/lib/button";
import { LoadingOutlined, RedoOutlined } from "@ant-design/icons";

//! User Files

import useTableSearch from "common/hooks/useTableSearch";
import api from "common/api";
import { LEAVE_STATUS, TOKEN } from "common/constants";
import { AppContext } from "AppContext";
import DeleteLeave from "./DeleteLeave";

function MyLeaves() {
  const {
    state: { userId },
  } = useContext(AppContext);
  const [loading, setLoading] = useState(false);
  const [leaveRequestData, setLeaveRequestData] = useState([]);
  const [getColumnSearchProps] = useTableSearch();

  const handleLeaveListUpdate = (leaveId) => {
    setLeaveRequestData(
      leaveRequestData.filter((data) => data.leaveId !== leaveId)
    );
  };

  const fetchLeavesList = async () => {
    setLoading(true);
    try {
      const response = await api.get(`/staff/leave/user/${userId}`, {
        headers: {
          Authorization: `Bearer ${localStorage.getItem(TOKEN)}`,
        },
      });
      const { data } = response;
      let leaveData = [];
      data?.forEach((leave) => {
        leaveData.push({
          leaveId: leave.leaveId,
          toDate: leave.toDate,
          fromDate: leave.fromDate,
          reason: leave.reason,
          requestStatus: leave.requestStatus,
          firstName: leave.userMetaResponse.firstName,
          lastName: leave.userMetaResponse.lastName,
          phone: leave.userMetaResponse.phone,
          email: leave.userMetaResponse.email,
        });
      });
      setLeaveRequestData(leaveData);
    } catch (error) {
      console.log(error);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchLeavesList();
    // eslint-disable-next-line
  }, []);

  const columns = [
    {
      title: "From",
      dataIndex: "fromDate",
      key: "fromDate",
      ...getColumnSearchProps("fromDate"),
      render: (fromDate) => {
        return <span>{moment(fromDate).format("Do MMM YYYY")}</span>;
      },
    },
    {
      title: "To",
      dataIndex: "toDate",
      key: "toDate",
      ...getColumnSearchProps("toDate"),
      render: (toDate) => {
        return <span>{moment(toDate).format("Do MMM YYYY")}</span>;
      },
    },
    {
      title: "Reason",
      dataIndex: "reason",
      key: "reason",
      ...getColumnSearchProps("reason"),
    },
    {
      title: "Status",
      dataIndex: "requestStatus",
      key: "requestStatus",
    },
    {
      title: "Action",
      key: "action",
      render: (record) => {
        return (
          record.requestStatus === LEAVE_STATUS.PENDING && (
            <DeleteLeave
              handleLeaveListUpdate={handleLeaveListUpdate}
              record={record}
            />
          )
        );
      },
    },
  ];

  const title = (
    <div className="cb-flex-sb">
      <span className="cb-text-strong">My Leaves</span>
      <span className="cb-flex-sb">
        <Button
          type="primary"
          shape="circle"
          icon={<RedoOutlined spin={loading} />}
          onClick={() => fetchLeavesList()}
        />
      </span>
    </div>
  );

  return (
    <Card title={title} className="user-list-card">
      <Table
        loading={{ spinning: loading, indicator: <LoadingOutlined /> }}
        className="bank-user-table"
        columns={columns}
        pagination={false}
        dataSource={leaveRequestData}
        sticky
        rowKey="leaveId"
      />
    </Card>
  );
}

export default MyLeaves;
