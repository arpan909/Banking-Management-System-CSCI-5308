import { useState } from "react";

//! Ant Imports

import { Button, Form } from "antd";

//! Ant Icons

import { CheckOutlined } from "@ant-design/icons";

//! User Files

import api from "common/api";
import { toast } from "common/utils";
import AccountAcceptModal from "./AccountAcceptModal";

function AccountAccept({ record, handleUserListUpdate }) {
  const [modalState, setModalState] = useState({
    visible: false,
    confirmLoading: false,
  });

  const [form] = Form.useForm();

  const onCreate = () => {
    form
      .validateFields()
      .then(async (values) => {
        const { balance, creditScore } = values;
        if (!Number(balance) || !Number(creditScore)) {
          toast({
            message: "Please enter number only",
            type: "error",
          });
        } else {
          const accountData = {
            email: record.email,
            balance,
            creditScore,
          };
          setModalState({
            ...modalState,
            confirmLoading: true,
          });
          try {
            const response = await api.post("/account/user", accountData);
            const { data } = response;
            handleUserListUpdate(record.email);
            toast({
              message: data.message,
              type: "success",
            });
            form.resetFields();
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
        }
      })
      .catch((info) => {
        console.log("Validate Failed:", info);
      });
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
      <AccountAcceptModal
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

export default AccountAccept;
