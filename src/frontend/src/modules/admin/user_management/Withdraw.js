import { useState } from "react";

//! Ant Imports

import { Form, Input, Button, Typography } from "antd";

//! User Files

import { toast } from "common/utils";
import api from "common/api";
import { REGEX } from "common/constants";

const { Title } = Typography;

function Withdraw() {
  const [loading, setLoading] = useState(false);
  const [form] = Form.useForm();

  const onFinish = async (values) => {
    setLoading(true);
    try {
      const response = await api.post("/account/user/withdrawal", values);
      const { data } = response;
      toast({
        message: data.message,
        type: "success",
      });
      form.resetFields();
    } catch (err) {
      if (err.response?.data) {
        toast({
          message: err.response.data.error,
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
    <div className="login">
      <Title level={3} className="sdp-text-strong">
        Withdraw
      </Title>
      <Form
        name="normal_login"
        className="login-form form"
        onFinish={onFinish}
        form={form}
      >
        <Form.Item
          name="accountNumber"
          rules={[
            { required: true, message: "This field is required" },
            {
              pattern: REGEX.NUMBER,
              message: "Account number must be a number",
            },
          ]}
        >
          <Input placeholder="Enter Account Number" />
        </Form.Item>
        <Form.Item
          name="balance"
          rules={[
            { required: true, message: "This field is required" },
            {
              pattern: REGEX.NUMBER,
              message: "Balance must be a number",
            },
          ]}
        >
          <Input placeholder="Enter Balance" />
        </Form.Item>
        <Form.Item>
          <Button
            loading={loading}
            type="primary"
            htmlType="submit"
            className="login-form-button button"
          >
            Deduct Amount
          </Button>
        </Form.Item>
      </Form>
    </div>
  );
}

export default Withdraw;
