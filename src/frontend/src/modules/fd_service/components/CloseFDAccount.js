import { useState } from "react";

//! Ant Imports

import { Button } from "antd";

//! User Files

import { toast } from "common/utils";
import api from "common/api";
import { CloseCircleOutlined } from "@ant-design/icons";

function CloseFDAccount({ record }) {
  const [loading, setLoading] = useState(false);

  const handleClose = async (fdData) => {
    setLoading(true);
    try {
      const response = await api.post(
        `/services/term-deposit/close/${fdData.termDepositId}`
      );
      const { data } = response;
      if (data?.success) {
        toast({
          message: data.message,
          type: "success",
        });
        window.location.reload();
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
      setLoading(false);
    }
  };

  return (
    <Button
      loading={loading}
      type="ghost"
      icon={<CloseCircleOutlined />}
      onClick={() => handleClose(record)}
    />
  );
}

export default CloseFDAccount;
