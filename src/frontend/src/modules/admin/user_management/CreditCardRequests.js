import { useEffect, useState } from "react";

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
import {
  CREDIT_CARD_STATUS,
  MINIMUM_CREDIT_SCORE,
  TOKEN,
} from "common/constants";
import CreditCardAccept from "./components/credit_card/CreditCardAccept";
import CreditCardReject from "./components/credit_card/CreditCardReject";

function CreditCardRequests() {
  const [loading, setLoading] = useState(false);
  const [ccRequests, setCcRequests] = useState([]);
  const [currentPage, setCurrentPage] = useState(0);
  const [totalElements, setTotalElements] = useState(0);
  const [creditCardStatus, setCreditCardStatus] = useState(
    CREDIT_CARD_STATUS.PENDING
  );
  const [getColumnSearchProps] = useTableSearch();

  const pageSize = 6;

  const handleCCListUpdate = (creditCardNumber) => {
    setCcRequests(
      ccRequests.filter((user) => user.creditCardNumber !== creditCardNumber)
    );
  };

  const fetchCCRequestList = async (page) => {
    setLoading(true);
    try {
      const response = await api.get("/services/creditcards", {
        params: {
          creditCardStatus,
          page,
          size: pageSize,
        },
        headers: {
          Authorization: `Bearer ${localStorage.getItem(TOKEN)}`,
        },
      });
      const { data } = response;
      setTotalElements(data.totalElements);
      setCcRequests(data.content);
    } catch (error) {
      console.log(error);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchCCRequestList(currentPage);
    // eslint-disable-next-line
  }, [creditCardStatus]);

  const columns = [
    {
      title: "CC Number",
      dataIndex: "creditCardNumber",
      key: "creditCardNumber",
      width: 250,
      ...getColumnSearchProps("creditCardNumber"),
    },
    {
      title: "Credit Score",
      dataIndex: ["accountDetailResponse", "creditScore"],
      key: "creditScore",
      render: (creditScore) => {
        return <span>{creditScore}</span>;
      },
    },
    {
      title: "Account Number",
      dataIndex: ["accountDetailResponse", "accountNumber"],
      key: "accountNumber",
      render: (accountNumber) => {
        return <span>{accountNumber}</span>;
      },
    },
    {
      title: "Customer ID",
      dataIndex: ["accountDetailResponse", "userMetaResponse", "id"],
      key: "id",
      render: (id) => {
        return <span>{id}</span>;
      },
    },
    {
      title: "Customer Email",
      dataIndex: ["accountDetailResponse", "userMetaResponse", "email"],
      key: "email",
      render: (email) => {
        return <span>{email}</span>;
      },
    },
    {
      title: "Action",
      key: "action",
      fixed: "right",
      render: (record) => {
        return (
          <Space size="middle">
            {creditCardStatus !== CREDIT_CARD_STATUS.APPROVED &&
              record.accountDetailResponse.creditScore >=
                MINIMUM_CREDIT_SCORE && (
                <CreditCardAccept
                  record={record}
                  handleCCListUpdate={handleCCListUpdate}
                />
              )}
            {creditCardStatus !== CREDIT_CARD_STATUS.DECLINED && (
              <CreditCardReject
                record={record}
                handleCCListUpdate={handleCCListUpdate}
              />
            )}
          </Space>
        );
      },
    },
  ];

  const onPageChange = (page) => {
    setCurrentPage(page - 1);
    fetchCCRequestList(page - 1);
  };

  const handleMenuClick = ({ key }) => {
    setCurrentPage(0);
    setCreditCardStatus(key);
  };

  const menu = (
    <Menu onClick={handleMenuClick}>
      <Menu.Item key={CREDIT_CARD_STATUS.PENDING}>Pending</Menu.Item>
      <Menu.Item key={CREDIT_CARD_STATUS.APPROVED}>Approved</Menu.Item>
      <Menu.Item key={CREDIT_CARD_STATUS.DECLINED}>Declined</Menu.Item>
    </Menu>
  );

  const title = (
    <div className="cb-flex-sb">
      <span className="cb-text-strong">Credit Cards Requests List</span>
      <span className="cb-flex-sb">
        <Button
          type="primary"
          shape="circle"
          icon={<RedoOutlined spin={loading} />}
          onClick={() => fetchCCRequestList(currentPage)}
        />
        <Dropdown overlay={menu}>
          <Button>
            {creditCardStatus} <DownOutlined />
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
        dataSource={ccRequests}
        sticky
        rowKey="creditCardNumber"
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

export default CreditCardRequests;
