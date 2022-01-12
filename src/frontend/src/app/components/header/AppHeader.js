import { Typography } from "antd";
import Layout from "antd/lib/layout";

//! User Files

import UserProfile from "./UserProfile";

const { Header } = Layout;
const { Title } = Typography;

const AppHeader = () => {
  return (
    <Header>
      <div className="app-header-left">
        <Title level={4}>DALHOUSIE BANK</Title>
      </div>
      <div className="app-header-right">
        <UserProfile />
      </div>
    </Header>
  );
};

export default AppHeader;
