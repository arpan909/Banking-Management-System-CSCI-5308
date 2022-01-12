import { useState } from "react";

//! Ant Imports

import { Button, Form } from "antd";

//! Ant Icons

import { CheckOutlined } from "@ant-design/icons";

//! User Files

import api from "common/api";
import { toast } from "common/utils";
import ResignAcceptModal from "./ResignAcceptModal";

function ResignAccept({ record, handleResignListUpdate }) {
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
        resignId: record.resignId,
        requestStatus: "APPROVED",
      };
      const response = await api.put("/staff/resignation", updateData);
      const { data } = response;
      handleResignListUpdate(record.resignId);
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
      <ResignAcceptModal
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

export default ResignAccept;
