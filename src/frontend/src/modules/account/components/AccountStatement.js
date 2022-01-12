import { useState } from "react";

//! Ant Imports

import { Form, Button, Typography, DatePicker, Table } from "antd";

//! Ant Icons

import { LoadingOutlined } from "@ant-design/icons";

//! User Files

import { toast } from "common/utils";
import api from "common/api";
import moment from "moment";
import useTableSearch from "common/hooks/useTableSearch";

const { Title } = Typography;
const { RangePicker } = DatePicker;

function AccountStatement() {
  const [loading, setLoading] = useState(false);
  const [form] = Form.useForm();
  const [accountStatement, setAccountStatement] = useState([]);
  const [getColumnSearchProps] = useTableSearch();

  const onFinish = async (values) => {
    setLoading(true);
    const { rangePicker } = values;
    const fromDate = moment(rangePicker[0]).format("YYYY-MM-DD");
    const toDate = moment(rangePicker[1]).format("YYYY-MM-DD");
    try {
      const response = await api.get("/account/activity", {
        params: {
          fromDate,
          toDate,
        },
      });
      const { data } = response;
      setAccountStatement(data.data);
      toast({
        message: data.message,
        type: "success",
      });
      form.resetFields();
    } catch (err) {
      if (err.response?.data) {
        toast({
          message: err.response.data.message,
          type: "error",
        });
      } else {
        toast({
          message: "Something went wrong!",
          type: "error",
        });
      }
    }
    setLoading(false);
  };

  const rangeConfig = {
    rules: [
      {
        type: "array",
        required: true,
        message: "Please select date!",
      },
    ],
  };

  const columns = [
    {
      title: "Transaction Id",
      dataIndex: "activityId",
      key: "activityId",
    },
    {
      title: "Date",
      dataIndex: "updatedAt",
      key: "updatedAt",
      ...getColumnSearchProps("updatedAt"),
      render: (updatedAt) => {
        return <span>{moment(updatedAt).format("Do MMM YYYY")}</span>;
      },
    },
    {
      title: "Amount",
      dataIndex: "transactionAmount",
      key: "transactionAmount",
      render: (amount, record) => {
        return (
          <span
            className={
              record.activityType === "WITHDRAWAL"
                ? "debit cb-text-strong"
                : "credit cb-text-strong"
            }
          >
            {amount}
          </span>
        );
      },
    },
    {
      title: "Activity Type",
      dataIndex: "activityType",
      key: "activityType",
      ...getColumnSearchProps("activityType"),
      render: (activityType) => {
        return (
          <span>{activityType === "WITHDRAWAL" ? "Debit" : "Credit"}</span>
        );
      },
    },
    {
      title: "Comment",
      dataIndex: "comment",
      key: "comment",
      ...getColumnSearchProps("comment"),
    },
  ];

  const renderTable = (
    <div className="sdp-pd-1">
      <Table
        loading={{ spinning: loading, indicator: <LoadingOutlined /> }}
        className="bank-user-table"
        columns={columns}
        pagination={false}
        dataSource={accountStatement}
        sticky
        rowKey="activityId"
      />
    </div>
  );

  const disabledDate = (current) => {
    return moment().add(0, "days") <= current;
  };

  return (
    <div className="sdp-flex">
      <Title level={3} className="sdp-text-strong">
        Account Statement
      </Title>
      <Form
        name="normal_login"
        className="login-form form"
        onFinish={onFinish}
        form={form}
      >
        <Form.Item name="rangePicker" {...rangeConfig}>
          <RangePicker disabledDate={disabledDate} />
        </Form.Item>
        <Form.Item>
          <Button
            loading={loading}
            type="primary"
            htmlType="submit"
            className="login-form-button button"
          >
            Submit Request
          </Button>
        </Form.Item>
      </Form>
      {renderTable}
    </div>
  );
}

export default AccountStatement;
