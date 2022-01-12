import { useEffect, useState } from "react";
import moment from "moment";

//! Ant Imports

import Card from "antd/lib/card";
import Table from "antd/lib/table";
import Button from "antd/lib/button";
import { LoadingOutlined, RedoOutlined } from "@ant-design/icons";

//! User Files

import useTableSearch from "common/hooks/useTableSearch";
import api from "common/api";
import { TOKEN } from "common/constants";
import GetFDReceipt from "./GetFDReceipt";
import CloseFDAccount from "./CloseFDAccount";

function MyFdAccounts() {
  const [loading, setLoading] = useState(false);
  const [fdAccounts, setFdAccounts] = useState([]);
  const [getColumnSearchProps] = useTableSearch();

  const fetchFDAccountsList = async () => {
    setLoading(true);
    try {
      const response = await api.get("/services/term-deposit", {
        headers: {
          Authorization: `Bearer ${localStorage.getItem(TOKEN)}`,
        },
      });
      const { data } = response;
      setFdAccounts(data);
    } catch (error) {
      console.log(error);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchFDAccountsList();
    // eslint-disable-next-line
  }, []);

  const columns = [
    {
      title: "Initial Amount",
      dataIndex: "initialAmount",
      key: "initialAmount",
      ...getColumnSearchProps("initialAmount"),
    },
    {
      title: "Duration",
      dataIndex: "duration",
      key: "duration",
    },
    {
      title: "Rate Of Interest",
      dataIndex: "rateOfInterest",
      key: "rateOfInterest",
    },
    {
      title: "Start Date",
      dataIndex: "startDate",
      key: "startDate",
      ...getColumnSearchProps("startDate"),
      render: (startDate) => {
        return <span>{moment(startDate).format("Do MMM YYYY")}</span>;
      },
    },
    {
      title: "Maturity Date",
      dataIndex: "maturityDate",
      key: "maturityDate",
      ...getColumnSearchProps("maturityDate"),
      render: (maturityDate) => {
        return <span>{moment(maturityDate).format("Do MMM YYYY")}</span>;
      },
    },
    {
      title: "Status",
      dataIndex: "termDepositStatus",
      key: "termDepositStatus",
    },
    {
      title: "Maturity Amount",
      dataIndex: "maturityAmount",
      key: "maturityAmount",
      render: (maturityAmount) => {
        return <span>${parseFloat(maturityAmount).toFixed(2)}</span>;
      },
    },
    {
      title: "Receipt",
      key: "receipt",
      render: (record) => {
        return <GetFDReceipt record={record} />;
      },
    },
    {
      title: "Close FD",
      key: "closeFd",
      render: (record) => {
        return record.termDepositStatus === "ACTIVE" ? (
          <CloseFDAccount record={record} />
        ) : (
          <Button loading={loading} type="text">
            -
          </Button>
        );
      },
    },
  ];

  const title = (
    <div className="cb-flex-sb">
      <span className="cb-text-strong">My FDs</span>
      <span className="cb-flex-sb">
        <Button
          type="primary"
          shape="circle"
          icon={<RedoOutlined spin={loading} />}
          onClick={() => fetchFDAccountsList()}
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
        dataSource={fdAccounts}
        sticky
        rowKey="termDepositId"
      />
    </Card>
  );
}

export default MyFdAccounts;
