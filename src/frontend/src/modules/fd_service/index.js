//! Ant Imports

import { Card } from "antd";
import { useState } from "react";
import MyFdAccounts from "./components/MyFdAccounts";
import OpenFDAccount from "./components/OpenFDAccount";

const tabList = [
  {
    key: "tab1",
    tab: "Open FD Account",
  },
  {
    key: "tab2",
    tab: "My FD Accounts",
  },
];

const contentList = {
  tab1: <OpenFDAccount />,
  tab2: <MyFdAccounts />,
};

function FDServices() {
  const [state, setState] = useState({
    key: "tab1",
  });

  const onTabChange = (key, type) => {
    setState({
      ...state,
      [type]: key,
    });
  };

  const title = <span className="cb-text-strong">FD Services</span>;

  return (
    <div>
      <Card
        style={{ width: "100%" }}
        title={title}
        tabList={tabList}
        activeTabKey={state.key}
        onTabChange={(key) => {
          onTabChange(key, "key");
        }}
      >
        <div
          style={{
            display: "flex",
            justifyContent: "center",
            padding: "2rem 0",
          }}
        >
          {contentList[state.key]}
        </div>
      </Card>
    </div>
  );
}

export default FDServices;
