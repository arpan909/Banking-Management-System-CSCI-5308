import { useContext, useEffect, useState } from "react";
import { useHistory } from "react-router-dom";

//! Ant Imports

import { Card, Button } from "antd";

//! User Files

import { AppContext } from "AppContext";
import moment from "moment";
import api from "common/api";
import { toast } from "common/utils";
import ServerError from "components/ServerError";
import Loading from "components/Loading";
import { ROUTES } from "common/constants";

const InnerTitle = ({ title }) => {
  return <span className="cb-text-strong">{title}</span>;
};

function Profile() {
  const {
    state: { currentUser, role, authToken },
  } = useContext(AppContext);

  const { push } = useHistory();

  const [loading, setLoading] = useState(false);
  const [err, setErr] = useState(false);
  const [userData, setUserData] = useState({});

  const bankUsername = currentUser?.username;
  const accountStatus = currentUser?.accountStatus;

  const customerId = userData?.id;
  const username = userData?.username;
  const firstName = userData?.firstName;
  const lastName = userData?.lastName;
  const address = userData?.address;
  const phone = userData?.phone;
  const email = userData?.email;
  const birthday = userData?.birthday;
  const zipCode = userData?.zipCode;
  const city = userData?.city;
  const state = userData?.state;

  const title = <span className="cb-text-strong">User Profile</span>;

  const handleUpdateClick = () => {
    push(ROUTES.UPDATE_PROFILE);
  };

  const fetchUserProfile = async () => {
    setLoading(true);
    try {
      const response = await api.get(`/users/${bankUsername}`, {
        headers: {
          Authorization: `Bearer ${authToken}`,
        },
      });
      const { data } = response;
      setUserData(data);
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
    fetchUserProfile();
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
          <Button type="link" onClick={handleUpdateClick}>
            Update
          </Button>
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
          title={<InnerTitle title="Account Status" />}
        >
          <span>{accountStatus}</span>
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
          title={<InnerTitle title="Birth Date" />}
        >
          <span>{moment(birthday).format("Do MMM YYYY")}</span>
        </Card>
        <Card
          type="inner"
          bordered={false}
          title={<InnerTitle title="Address" />}
        >
          <span>{address}</span>
        </Card>
        <Card
          type="inner"
          bordered={false}
          title={<InnerTitle title="Zip Code" />}
        >
          <span>{zipCode}</span>
        </Card>
        <Card type="inner" bordered={false} title={<InnerTitle title="City" />}>
          <span>{city}</span>
        </Card>
        <Card
          type="inner"
          bordered={false}
          title={<InnerTitle title="State" />}
        >
          <span>{state}</span>
        </Card>
        <Card
          type="inner"
          bordered={false}
          title={<InnerTitle title="User Type" />}
        >
          <span>{role.split("_")[1]}</span>
        </Card>
      </Card>
    </div>
  );
}

export default Profile;
