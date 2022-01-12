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
import { RESIGN_STATUS, TOKEN } from "common/constants";
import ResignAccept from "./components/ResignAccept";
import ResignReject from "./components/ResignReject";

function ResignationRequests() {
  const [loading, setLoading] = useState(false);
  const [resignRequestData, setResignRequestData] = useState([]);
  const [currentPage, setCurrentPage] = useState(0);
  const [totalElements, setTotalElements] = useState(0);
  const [resignStatus, setResignStatus] = useState(RESIGN_STATUS.PENDING);
  const [getColumnSearchProps] = useTableSearch();

  const pageSize = 6;

  const handleResignListUpdate = (resignId) => {
    setResignRequestData(
      resignRequestData.filter((data) => data.resignId !== resignId)
    );
  };
  const fetchResignList = async (page) => {
    setLoading(true);
    try {
      const response = await api.get("/staff/resignation", {
        params: {
          requestStatus: resignStatus,
          page,
          size: pageSize,
        },
        headers: {
          Authorization: `Bearer ${localStorage.getItem(TOKEN)}`,
        },
      });
      const { data } = response;
      setTotalElements(data.totalElements);
      let resignData = [];
      data?.content?.forEach((resign) => {
        resignData.push({
          resignId: resign.resignId,
          date: resign.date,
          reason: resign.reason,
          firstName: resign.userMetaResponse.firstName,
          lastName: resign.userMetaResponse.lastName,
          phone: resign.userMetaResponse.phone,
          email: resign.userMetaResponse.email,
        });
      });
      setResignRequestData(resignData);
    } catch (error) {
      console.log(error);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchResignList(currentPage);
    // eslint-disable-next-line
  }, [resignStatus]);

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
      title: "Date",
      dataIndex: "date",
      key: "date",
      ...getColumnSearchProps("date"),
      render: (date) => {
        return <span>{moment(date).format("Do MMM YYYY")}</span>;
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
          {resignStatus !== RESIGN_STATUS.APPROVED && (
            <ResignAccept
              record={record}
              handleResignListUpdate={handleResignListUpdate}
            />
          )}
          {resignStatus !== RESIGN_STATUS.REJECTED && (
            <ResignReject
              record={record}
              handleResignListUpdate={handleResignListUpdate}
            />
          )}
        </Space>
      ),
    },
  ];

  const onPageChange = (page) => {
    setCurrentPage(page - 1);
    fetchResignList(page - 1);
  };

  const handleMenuClick = ({ key }) => {
    setCurrentPage(0);
    setResignStatus(key);
  };

  const menu = (
    <Menu onClick={handleMenuClick}>
      <Menu.Item key={RESIGN_STATUS.PENDING}>Pending</Menu.Item>
      <Menu.Item key={RESIGN_STATUS.APPROVED}>Approved</Menu.Item>
      <Menu.Item key={RESIGN_STATUS.REJECTED}>Rejected</Menu.Item>
    </Menu>
  );

  const title = (
    <div className="cb-flex-sb">
      <span className="cb-text-strong">Resignations List</span>
      <span className="cb-flex-sb">
        <Button
          type="primary"
          shape="circle"
          icon={<RedoOutlined spin={loading} />}
          onClick={() => fetchResignList(currentPage)}
        />
        <Dropdown overlay={menu}>
          <Button>
            {resignStatus} <DownOutlined />
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
        dataSource={resignRequestData}
        scroll={{ x: 1500 }}
        sticky
        rowKey="email"
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

export default ResignationRequests;
