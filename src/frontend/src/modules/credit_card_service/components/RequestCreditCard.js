import { useState } from "react";

//! Ant Imports

import { Form, Input, Button } from "antd";

//! User Files

import { toast } from "common/utils";
import api from "common/api";

function RequestCreditCard() {
  const [loading, setLoading] = useState(false);
  const [form] = Form.useForm();

  const onFinish = async (values) => {
    setLoading(true);
    if (isNaN(values.expectedTransactionLimit)) {
      toast({
        message: "Transaction limit must be a digit",
        type: "error",
      });
    } else {
      try {
        const response = await api.post("/services/creditcards", values);
        const { data } = response;
        if (data?.creditCardNumber) {
          toast({
            message:
              "Credit Card Request made successfully! If it is approved, card will be delivered to your address",
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
          name="expectedTransactionLimit"
          rules={[{ required: true, message: "This field is required" }]}
        >
          <Input placeholder="Enter expected card limit" />
        </Form.Item>
        <Form.Item>
          <Button
            loading={loading}
            type="primary"
            htmlType="submit"
            className="login-form-button button"
          >
            Submit Request
          </Button>
        </Form.Item>
      </Form>
    </div>
  );
}

export default RequestCreditCard;
