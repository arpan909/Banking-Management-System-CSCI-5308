import { useState } from "react";

//! Ant Imports

import { Button } from "antd";

//! Ant Design

import FDReceiptModal from "./FDReceiptModal";

function GetFDReceipt({ record }) {
  const [visible, setVisible] = useState(false);

  return (
    <>
      <Button
        type="dashed"
        onClick={() => {
          setVisible(true);
        }}
      >
        Receipt
      </Button>
      <FDReceiptModal
        record={record}
        // onCreate={onCreate}
        visible={visible}
        onCancel={() => {
          setVisible(false);
        }}
      />
    </>
  );
}

export default GetFDReceipt;
