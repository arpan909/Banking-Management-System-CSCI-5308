import { useState } from "react";

//! Ant Imports

import { Button } from "antd";

//! Ant Icons

import { CloseOutlined } from "@ant-design/icons";

//! User Files

import api from "common/api";
import { toast } from "common/utils";
import CreditCardRejectModal from "./CreditCardRejectModal";

function CreditCardReject({ record, handleCCListUpdate }) {
  const [modalState, setModalState] = useState({
    visible: false,
    confirmLoading: false,
  });

  const onCreate = async () => {
    setModalState({
      ...modalState,
      confirmLoading: true,
    });
    try {
      const updateData = {
        creditCardNumber: record.creditCardNumber,
        creditCardStatus: "DECLINED",
      };
      const response = await api.put("/services/creditcards", updateData);
      const { data } = response;
      handleCCListUpdate(record.creditCardNumber);
      toast({
        message: data.message,
        type: "success",
      });
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
      setModalState({
        visible: false,
        confirmLoading: false,
      });
    }
  };
  return (
    <>
      <Button
        type="dashed"
        shape="circle"
        icon={<CloseOutlined />}
        onClick={() => {
          setModalState({
            ...modalState,
            visible: true,
          });
        }}
      />
      <CreditCardRejectModal
        record={record}
        modalState={modalState}
        onCreate={onCreate}
        onCancel={() => {
          setModalState({
            ...modalState,
            visible: false,
          });
        }}
      />
    </>
  );
}

export default CreditCardReject;
