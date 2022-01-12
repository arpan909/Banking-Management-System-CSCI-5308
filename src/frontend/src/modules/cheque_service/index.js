//! Ant Imports

import { Card } from "antd";
import { useState } from "react";

const tabList = [
  {
    key: "tab1",
    tab: "Request a New Chequebook",
  },
  {
    key: "tab2",
    tab: "Status Enquiry",
  },
  {
    key: "tab3",
    tab: "Stop Cheque",
  },
];

const contentList = {
  tab1: <p>Coming Soon</p>,
  tab2: <p>Coming Soon</p>,
  tab3: <p>Coming Soon</p>,
};

function ChequeServices() {
  const [state, setState] = useState({
    key: "tab1",
  });

  const onTabChange = (key, type) => {
    setState({
      ...state,
      [type]: key,
    });
  };

  const title = <span className="cb-text-strong">Cheque Services</span>;

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

export default ChequeServices;
