import { useState } from "react";

//! Ant Imports

import { Button, Form } from "antd";

//! Ant Icons

import { CheckOutlined } from "@ant-design/icons";

//! User Files

import api from "common/api";
import { toast } from "common/utils";
import LeaveAcceptModal from "./LeaveAcceptModal";

function LeaveAccept({ record, handleLeaveListUpdate }) {
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
        leaveId: record.leaveId,
        requestStatus: "APPROVED",
      };
      const response = await api.put("/staff/leave", updateData);
      const { data } = response;
      handleLeaveListUpdate(record.leaveId);
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
      <LeaveAcceptModal
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

export default LeaveAccept;
