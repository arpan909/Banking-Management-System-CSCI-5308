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
import { ACCOUNT_STATUS, TOKEN } from "common/constants";
import AccountAccept from "./components/account/AccountAccept";
import AccountReject from "./components/account/AccountReject";

function AccountOpeningRequests() {
  const [loading, setLoading] = useState(false);
  const [users, setUsers] = useState([]);
  const [currentPage, setCurrentPage] = useState(0);
  const [totalElements, setTotalElements] = useState(0);
  const [accountStatus, setAccountStatus] = useState(ACCOUNT_STATUS.PENDING);
  const [getColumnSearchProps] = useTableSearch();

  const pageSize = 6;

  const handleUserListUpdate = (email) => {
    setUsers(users.filter((user) => user.email !== email));
  };

  const fetchAccountList = async (page) => {
    setLoading(true);
    try {
      const response = await api.get("/account/user", {
        params: {
          accountStatus,
          page,
          size: pageSize,
        },
        headers: {
          Authorization: `Bearer ${localStorage.getItem(TOKEN)}`,
        },
      });
      const { data } = response;
      setTotalElements(data.totalElements);
      setUsers(data.content);
    } catch (error) {
      console.log(error);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchAccountList(currentPage);
    // eslint-disable-next-line
  }, [accountStatus]);

  const columns = [
    {
      title: "Customer ID",
      dataIndex: "id",
      key: "id",
      ...getColumnSearchProps("id"),
      render: (id) => {
        return <span>{`DAL${id}`}</span>;
      },
    },
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
      title: "Email",
      dataIndex: "email",
      key: "email",
      ...getColumnSearchProps("email"),
    },
    {
      title: "Requested Account Type",
      dataIndex: "requestedAccountType",
      key: "requestedAccountType",
      sorter: (a, b) =>
        a.requestedAccountType.localeCompare(b.requestedAccountType),
      ...getColumnSearchProps("requestedAccountType"),
    },
    {
      title: "Phone",
      dataIndex: "phone",
      key: "phone",
      ...getColumnSearchProps("phone"),
    },
    {
      title: "Address",
      dataIndex: "address",
      key: "address",
      ellipsis: { showTitle: true },
      ...getColumnSearchProps("address"),
    },
    {
      title: "Birthday",
      dataIndex: "birthday",
      key: "birthday",
      ...getColumnSearchProps("birthday"),
      render: (birthday) => {
        return <span>{moment(birthday).format("Do MMM YYYY")}</span>;
      },
    },
    {
      title: "Action",
      key: "action",
      fixed: "right",
      render: (record) => (
        <Space size="middle">
          <AccountAccept
            record={record}
            handleUserListUpdate={handleUserListUpdate}
          />
          {accountStatus !== ACCOUNT_STATUS.REJECTED && (
            <AccountReject
              record={record}
              handleUserListUpdate={handleUserListUpdate}
            />
          )}
        </Space>
      ),
    },
  ];

  const onPageChange = (page) => {
    setCurrentPage(page - 1);
    fetchAccountList(page - 1);
  };

  const handleMenuClick = ({ key }) => {
    setCurrentPage(0);
    setAccountStatus(key);
  };

  const menu = (
    <Menu onClick={handleMenuClick}>
      <Menu.Item key={ACCOUNT_STATUS.PENDING}>Pending</Menu.Item>
      <Menu.Item key={ACCOUNT_STATUS.REJECTED}>Rejected</Menu.Item>
    </Menu>
  );

  const title = (
    <div className="cb-flex-sb">
      <span className="cb-text-strong">Users List</span>
      <span className="cb-flex-sb">
        <Button
          type="primary"
          shape="circle"
          icon={<RedoOutlined spin={loading} />}
          onClick={() => fetchAccountList(currentPage)}
        />
        <Dropdown overlay={menu}>
          <Button>
            {accountStatus} <DownOutlined />
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
        dataSource={users}
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

export default AccountOpeningRequests;
