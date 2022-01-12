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
import DeleteResign from "./DeleteResign";

function MyResign() {
  const {
    state: { userId },
  } = useContext(AppContext);
  const [loading, setLoading] = useState(false);
  const [resignRequestData, setResignRequestData] = useState([]);
  const [getColumnSearchProps] = useTableSearch();

  const handleResignListUpdate = (resignId) => {
    setResignRequestData(
      resignRequestData.filter((data) => data.resignId !== resignId)
    );
  };

  const fetchResignList = async () => {
    setLoading(true);
    try {
      const response = await api.get(`/staff/resignation/user/${userId}`, {
        headers: {
          Authorization: `Bearer ${localStorage.getItem(TOKEN)}`,
        },
      });
      const { data } = response;
      let resignData = [];
      data?.forEach((resign) => {
        resignData.push({
          resignId: resign.resignId,
          date: resign.date,
          reason: resign.reason,
          requestStatus: resign.requestStatus,
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
    fetchResignList();
    // eslint-disable-next-line
  }, []);

  const columns = [
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
            <DeleteResign
              handleResignListUpdate={handleResignListUpdate}
              record={record}
            />
          )
        );
      },
    },
  ];

  const title = (
    <div className="cb-flex-sb">
      <span className="cb-text-strong">My Resign Request</span>
      <span className="cb-flex-sb">
        <Button
          type="primary"
          shape="circle"
          icon={<RedoOutlined spin={loading} />}
          onClick={() => fetchResignList()}
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
        dataSource={resignRequestData}
        sticky
        rowKey="resignId"
      />
    </Card>
  );
}

export default MyResign;
