import { useState } from "react";

//! Ant Imports

import { Button } from "antd";

//! Ant Icons

import { CloseOutlined } from "@ant-design/icons";

//! User Files

import api from "common/api";
import { toast } from "common/utils";
import LeaveRejectModal from "./LeaveRejectModal";

function LeaveReject({ record, handleLeaveListUpdate }) {
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
        leaveId: record.leaveId,
        requestStatus: "REJECTED",
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
      <LeaveRejectModal
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

export default LeaveReject;
