import { useState } from "react";

//! Ant Imports

import { Form, Input, Button } from "antd";

//! User Files

import { toast } from "common/utils";
import api from "common/api";

function SetPin({ debitCardNumber }) {
  const [loading, setLoading] = useState(false);
  const [form] = Form.useForm();

  const onFinish = async (values) => {
    setLoading(true);
    const { pin } = values;
    values.debitCardNumber = debitCardNumber;
    if (isNaN(pin)) {
      toast({
        message: "Pin must be a digit",
        type: "error",
      });
    } else {
      try {
        const response = await api.put("/services/debitcard/pin", values);
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
