import { useState } from "react";

//! Ant Imports

import { Form, Input, Button } from "antd";

//! User Files

import { toast } from "common/utils";
import api from "common/api";

function OpenFDAccount() {
  const [loading, setLoading] = useState(false);
  const [form] = Form.useForm();

  const onFinish = async (values) => {
    setLoading(true);
    const { years, initialAmount } = values;
    if (isNaN(years) || isNaN(initialAmount)) {
      toast({
        message: "Year and amount must be a digit",
        type: "error",
      });
    } else if (values.years > 50) {
      toast({
        message: "FD Period can not be more than 50",
        type: "info",
      });
    } else {
      try {
        const response = await api.post("/services/term-deposit", values);
        const { data } = response;
        if (data?.success) {
          toast({
            message: "FD Account opened successfully",
            type: "success",
          });
          form.resetFields();
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
      }
    }
    setLoading(false);
  };

  return (
    <div>
      <Form
        form={form}
        name="normal_login"
        className="login-form form"
        onFinish={onFinish}
      >
        <Form.Item
          name="initialAmount"
          rules={[{ required: true, message: "This field is required" }]}
        >
          <Input placeholder="Enter FD amount" />
        </Form.Item>
        <Form.Item
          name="years"
          rules={[{ required: true, message: "This field is required" }]}
        >
          <Input placeholder="Enter FD period in years" />
        </Form.Item>
        <Form.Item>
          <Button
            loading={loading}
            type="primary"
            htmlType="submit"
            className="login-form-button button"
          >
            Open FD Account
          </Button>
        </Form.Item>
      </Form>
    </div>
  );
}

export default OpenFDAccount;
