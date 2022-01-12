//! Ant Imports

import { Card } from "antd";
import { useState } from "react";

//! User Imports
import RequestCreditCard from "./components/RequestCreditCard";
import SetPin from "./components/SetPin";

const tabList = [
  {
    key: "tab1",
    tab: "Request a Credit Card",
  },
  // {
  //   key: "tab2",
  //   tab: "Pay Credit Card Bills",
  // },
  {
    key: "tab3",
    tab: "Set PIN",
  },
];

const contentList = {
  tab1: <RequestCreditCard />,
  // tab2: <p>content2</p>,
  tab3: <SetPin />,
};

function CreditCardServices() {
  const [state, setState] = useState({
    key: "tab1",
  });

  const onTabChange = (key, type) => {
    setState({
      ...state,
      [type]: key,
    });
  };

  const title = <span className="cb-text-strong">Credit Card Services</span>;

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

export default CreditCardServices;
