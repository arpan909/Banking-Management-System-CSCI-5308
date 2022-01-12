import { useState } from "react";

//! Ant Imports

import { Form, Input, Button } from "antd";

//! User Files

import { toast } from "common/utils";
import api from "common/api";

function SetPin() {
  const [loading, setLoading] = useState(false);
  const [form] = Form.useForm();

  const onFinish = async (values) => {
    setLoading(true);
    const { creditCardNumber, pin } = values;
    if (isNaN(pin) || isNaN(creditCardNumber)) {
      toast({
        message: "Pin and Card Number must be a digit",
        type: "error",
      });
    } else {
      try {
        const response = await api.put("/services/creditcards/pin", values);
        const { data } = response;
        if (data?.success) {
          toast({
            message: data.message,
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
          name="creditCardNumber"
          rules={[
            { required: true, message: "This field is required" },
            { len: 16, message: "Card number must be of 16 numbers" },
          ]}
        >
          <Input placeholder="Enter Card Number" />
        </Form.Item>
        <Form.Item
          name="pin"
          rules={[
            { required: true, message: "This field is required" },
            { len: 4, message: "Pin must be of 4 numbers" },
          ]}
        >
          <Input placeholder="Enter Pin" />
        </Form.Item>
        <Form.Item>
          <Button
            loading={loading}
            type="primary"
            htmlType="submit"
            className="login-form-button button"
          >
            Set Pin
          </Button>
        </Form.Item>
      </Form>
    </div>
  );
}

export default SetPin;
