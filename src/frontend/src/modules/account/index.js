import { useContext, useEffect, useState } from "react";
import { useHistory } from "react-router-dom";

//! Ant Imports

import { Button, Card, Popconfirm } from "antd";

//! User Files

import moment from "moment";
import api from "common/api";
import { toast } from "common/utils";
import ServerError from "components/ServerError";
import Loading from "components/Loading";
import { AppContext } from "AppContext";
import { ROUTES } from "common/constants";

const InnerTitle = ({ title }) => {
  return <span className="cb-text-strong">{title}</span>;
};

function Account() {
  const {
    state: { authToken },
  } = useContext(AppContext);
  const { push } = useHistory();
  const [loading, setLoading] = useState(false);
  const [err, setErr] = useState(false);
  const [accountData, setAccountData] = useState({});
  const [visible, setVisible] = useState(false);
  const [confirmLoading, setConfirmLoading] = useState(false);

  const showPopconfirm = () => {
    setVisible(true);
  };

  const handleCloseSavingAccount = async () => {
    setConfirmLoading(true);
    try {
      const response = await api.put("/users/account/status/close");
      const { data } = response;
      toast({
        message: data.message,
        type: "success",
      });
      push(ROUTES.LOGOUT);
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
    } finally {
      setConfirmLoading(false);
    }
  };

  const handleCancel = () => {
    setVisible(false);
  };

  const accountNumber = accountData?.accountNumber;
  const accountCreatedAt = accountData?.accountCreatedAt;
  const lastActivityAt = accountData?.lastActivityAt;
  const balance = accountData?.balance;
  const creditScore = accountData?.creditScore;
  const accountType = accountData?.accountType;
  const user = accountData?.userMetaResponse;
  const customerId = user?.id;
  const username = user?.username;
  const firstName = user?.firstName;
  const lastName = user?.lastName;
  const phone = user?.phone;
  const email = user?.email;
  const debitCardNumber = accountData?.debitCardNumber;

  const title = <span className="cb-text-strong">My Account</span>;

  const fetchUserAccount = async () => {
    setLoading(true);
    try {
      const response = await api.get(`/account/me`, {
        headers: {
          Authorization: `Bearer ${authToken}`,
        },
      });
      const { data } = response;
      setAccountData(data);
    } catch (err) {
      setErr(true);
      toast({
        message: "Something went wrong!",
        type: "error",
      });
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchUserAccount();
    // eslint-disable-next-line
  }, []);

  if (loading) return <Loading />;
  if (err) return <ServerError />;
  return (
    <div className="profile">
      <Card
        title={title}
        className="profile-card"
        extra={
          <Popconfirm
            title="You sure, you want to close account?"
            visible={visible}
            okText="Close"
            onConfirm={handleCloseSavingAccount}
            okButtonProps={{ loading: confirmLoading }}
            onCancel={handleCancel}
            overlayClassName="my-pop"
          >
            <Button type="ghost" onClick={showPopconfirm}>
              Close Saving Account
            </Button>
          </Popconfirm>
        }
      >
        <Card
          type="inner"
          bordered={false}
          title={<InnerTitle title="Customer ID" />}
        >
          <span>{`DAL${customerId}`}</span>
        </Card>
        <Card
          type="inner"
          bordered={false}
          title={<InnerTitle title="Account Number" />}
        >
          <span>{accountNumber}</span>
        </Card>
        <Card
          type="inner"
          bordered={false}
          title={<InnerTitle title="Balance" />}
        >
          <span>{balance}</span>
        </Card>
        <Card
          type="inner"
          bordered={false}
          title={<InnerTitle title="Credit Score" />}
        >
          <span>{creditScore}</span>
        </Card>
        <Card
          type="inner"
          bordered={false}
          title={<InnerTitle title="Account Type" />}
        >
          <span>{accountType}</span>
        </Card>
        <Card
          type="inner"
          bordered={false}
          title={<InnerTitle title="Username" />}
        >
          <span>{username}</span>
        </Card>
        <Card type="inner" bordered={false} title={<InnerTitle title="Name" />}>
          <span>{`${firstName} ${lastName}`}</span>
        </Card>
        <Card
          type="inner"
          bordered={false}
          title={<InnerTitle title="Email" />}
        >
          <span>{email}</span>
        </Card>
        <Card
          type="inner"
          bordered={false}
          title={<InnerTitle title="Phone" />}
        >
          <span>{phone}</span>
        </Card>
        <Card
          type="inner"
          bordered={false}
          title={<InnerTitle title="Debit Card Number" />}
        >
          <span>{debitCardNumber}</span>
        </Card>
        <Card
          type="inner"
          bordered={false}
          title={<InnerTitle title="Account Creation Date" />}
        >
          <span>
            {moment(accountCreatedAt).format("Do MMM YYYY hh:mm:ss A")}
          </span>
        </Card>
        <Card
          type="inner"
          bordered={false}
          title={<InnerTitle title="Account Last Activity" />}
        >
          <span>{moment(lastActivityAt).format("Do MMM YYYY hh:mm:ss A")}</span>
        </Card>
      </Card>
    </div>
  );
}

export default Account;
