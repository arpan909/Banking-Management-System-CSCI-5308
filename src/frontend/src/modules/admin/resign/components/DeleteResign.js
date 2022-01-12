import { useState } from "react";

//! Ant Imports

import Space from "antd/lib/space";
import Button from "antd/lib/button";
import Popconfirm from "antd/lib/popconfirm";
import { DeleteFilled } from "@ant-design/icons";

//! User Files

import api from "common/api";
import { toast } from "common/utils";

function DeleteResign({ handleResignListUpdate, record }) {
  const [visible, setVisible] = useState(false);
  const [deleteLoading, setDeleteLoading] = useState(false);
  const handleResignDelete = async (record) => {
    setDeleteLoading(true);
    try {
      const response = await api.delete(
        `/staff/resignation/${record.resignId}`
      );
      const { data } = response;
      if (data?.success) {
        handleResignListUpdate(record.resignId);
        toast({
          message: data.message,
          type: "success",
        });
      }
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
      setVisible(false);
      setDeleteLoading(false);
    }
  };

  const showPopconfirm = () => {
    setVisible(true);
  };

  const handleCancel = () => {
    setVisible(false);
  };
  return (
    <Space size="middle">
      <Popconfirm
        title="Are you sure to delete this resign request?"
        visible={visible}
        okButtonProps={{ loading: deleteLoading }}
        onConfirm={() => handleResignDelete(record)}
        onCancel={handleCancel}
        cancelButtonProps={{ disabled: deleteLoading }}
        okText="Yes"
        cancelText="No"
        overlayClassName="my-pop"
      >
        <Button
          type="text"
          shape="circle"
          icon={<DeleteFilled />}
          onClick={showPopconfirm}
        />
      </Popconfirm>
    </Space>
  );
}

export default DeleteResign;
