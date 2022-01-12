import { useState } from "react";

//! Ant Imports

import { Button, Form } from "antd";

//! Ant Icons

import { CheckOutlined } from "@ant-design/icons";

//! User Files

import api from "common/api";
import { toast } from "common/utils";
import CreditCardAcceptModal from "./CreditCardAcceptModal";

function CreditCardAccept({ record, handleCCListUpdate }) {
  const [modalState, setModalState] = useState({
    visible: false,
    confirmLoading: false,
  });

  const [form] = Form.useForm();

  const onCreate = async () => {
    setModalState({
      ...modalState,
      confirmLoading: true,
    });
    try {
      const updateData = {
        creditCardNumber: record.creditCardNumber,
        creditCardStatus: "APPROVED",
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
        type="primary"
        shape="circle"
        icon={<CheckOutlined />}
        onClick={() => {
          setModalState({
            ...modalState,
            visible: true,
          });
        }}
      />
      <CreditCardAcceptModal
        record={record}
        modalState={modalState}
        onCreate={onCreate}
        onCancel={() => {
          setModalState({
            ...modalState,
            visible: false,
          });
        }}
        form={form}
      />
    </>
  );
}

export default CreditCardAccept;
