import { useState } from "react";

//! Ant Imports

import { Form, Button, Select } from "antd";

//! User Files

import { toast } from "common/utils";
import api from "common/api";

const { Option } = Select;

function SetDebitCardStatus({ debitCardNumber }) {
  const [loading, setLoading] = useState(false);
  const [form] = Form.useForm();

  const onFinish = async (values) => {
    setLoading(true);
    values.debitCardNumber = debitCardNumber;
    try {
      const response = await api.put("/services/debitcard/status", values);
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
    } finally {
      setLoading(false);
    }
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
          name="debitCardStatus"
          rules={[{ required: true, message: "This field is required" }]}
        >
          <Select allowClear placeholder="Debit Card Status">
            <Option value="ACTIVE">Active</Option>
            <Option value="BLOCKED">Blocked</Option>
          </Select>
        </Form.Item>
        <Form.Item>
          <Button
            loading={loading}
            type="primary"
            htmlType="submit"
            className="login-form-button button"
          >
            Change Status
          </Button>
        </Form.Item>
      </Form>
    </div>
  );
}

export default SetDebitCardStatus;
