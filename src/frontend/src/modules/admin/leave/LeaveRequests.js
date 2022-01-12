import { useEffect, useState } from "react";
import moment from "moment";

//! Ant Imports

import Card from "antd/lib/card";
import Table from "antd/lib/table";
import Pagination from "antd/lib/pagination";
import Space from "antd/lib/space";
import Button from "antd/lib/button";
import Dropdown from "antd/lib/dropdown";
import Menu from "antd/lib/menu";
import { DownOutlined, LoadingOutlined, RedoOutlined } from "@ant-design/icons";

//! User Files

import useTableSearch from "common/hooks/useTableSearch";
import api from "common/api";
import { LEAVE_STATUS, TOKEN } from "common/constants";
import LeaveAccept from "./components/LeaveAccept";
import LeaveReject from "./components/LeaveReject";

function LeaveRequests() {
  const [loading, setLoading] = useState(false);
  const [leaveRequestData, setLeaveRequestData] = useState([]);
  const [currentPage, setCurrentPage] = useState(0);
  const [totalElements, setTotalElements] = useState(0);
  const [leaveStatus, setLeaveStatus] = useState(LEAVE_STATUS.PENDING);
  const [getColumnSearchProps] = useTableSearch();

  const pageSize = 6;

  const handleLeaveListUpdate = (leaveId) => {
    setLeaveRequestData(
      leaveRequestData.filter((data) => data.leaveId !== leaveId)
    );
  };
  const fetchLeavesList = async (page) => {
    setLoading(true);
    try {
      const response = await api.get("/staff/leave", {
        params: {
          requestStatus: leaveStatus,
          page,
          size: pageSize,
        },
        headers: {
          Authorization: `Bearer ${localStorage.getItem(TOKEN)}`,
        },
      });
      const { data } = response;
      setTotalElements(data.totalElements);
      let leaveData = [];
      data?.content?.forEach((leave) => {
        leaveData.push({
          leaveId: leave.leaveId,
          toDate: leave.toDate,
          fromDate: leave.fromDate,
          reason: leave.reason,
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
    fetchLeavesList(currentPage);
    // eslint-disable-next-line
  }, [leaveStatus]);

  const columns = [
    {
      title: "First Name",
      dataIndex: "firstName",
      key: "firstName",
      ...getColumnSearchProps("firstName"),
    },
    {
      title: "Last Name",
      dataIndex: "lastName",
      key: "lastName",
      ...getColumnSearchProps("lastName"),
    },
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
      title: "Email",
      dataIndex: "email",
      key: "email",
      ...getColumnSearchProps("email"),
    },
    {
      title: "Phone",
      dataIndex: "phone",
      key: "phone",
      ...getColumnSearchProps("phone"),
    },
    {
      title: "Action",
      key: "action",
      fixed: "right",
      render: (record) => (
        <Space size="middle">
          {leaveStatus !== LEAVE_STATUS.APPROVED && (
            <LeaveAccept
              record={record}
              handleLeaveListUpdate={handleLeaveListUpdate}
            />
          )}
          {leaveStatus !== LEAVE_STATUS.REJECTED && (
            <LeaveReject
              record={record}
              handleLeaveListUpdate={handleLeaveListUpdate}
            />
          )}
        </Space>
      ),
    },
  ];

  const onPageChange = (page) => {
    setCurrentPage(page - 1);
    fetchLeavesList(page - 1);
  };

  const handleMenuClick = ({ key }) => {
    setCurrentPage(0);
    setLeaveStatus(key);
  };

  const menu = (
    <Menu onClick={handleMenuClick}>
      <Menu.Item key={LEAVE_STATUS.PENDING}>Pending</Menu.Item>
      <Menu.Item key={LEAVE_STATUS.APPROVED}>Approved</Menu.Item>
      <Menu.Item key={LEAVE_STATUS.REJECTED}>Rejected</Menu.Item>
    </Menu>
  );

  const title = (
    <div className="cb-flex-sb">
      <span className="cb-text-strong">Leaves List</span>
      <span className="cb-flex-sb">
        <Button
          type="primary"
          shape="circle"
          icon={<RedoOutlined spin={loading} />}
          onClick={() => fetchLeavesList(currentPage)}
        />
        <Dropdown overlay={menu}>
          <Button>
            {leaveStatus} <DownOutlined />
          </Button>
        </Dropdown>
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
        scroll={{ x: 1500 }}
        sticky
        rowKey="leaveId"
      />
      <div className="user-pagination">
        <Pagination
          className="custom-pagination"
          disabled={loading}
          hideOnSinglePage
          defaultCurrent={1}
          total={totalElements}
          pageSize={pageSize}
          onChange={onPageChange}
        />
      </div>
    </Card>
  );
}

export default LeaveRequests;
