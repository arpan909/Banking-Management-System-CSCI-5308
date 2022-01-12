import { useState } from "react";

//! Ant Imports

import { Form, Input, Button, Typography, DatePicker } from "antd";

//! User Files

import { toast } from "common/utils";
import api from "common/api";
import moment from "moment";

const { Title } = Typography;
const { TextArea } = Input;

function ApplyResignation() {
  const date = new Date();
  const eligibleYear = date.getFullYear();
  const eligibleMonth = date.getMonth() + 2;
  const eligibleDate = date.getDate() + 1;
  const [loading, setLoading] = useState(false);

  const [form] = Form.useForm();

  const onFinish = async (values) => {
    setLoading(true);
    const { reason, date } = values;
    try {
      const response = await api.post("/staff/resignation", {
        reason,
        date,
      });
      const { data } = response;
      toast({
        message: data.message,
        type: "success",
      });
      form.resetFields();
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
    setLoading(false);
  };

  const disabledDate = (current) => {
    let customDate = `${eligibleYear}-${eligibleMonth}-${eligibleDate}`;
    return current && current < moment(customDate, "YYYY-MM-DD");
  };

  return (
    <div className="login">
      <Title level={3} className="sdp-text-strong">
        Apply Resignation
      </Title>
      <Form
        name="normal_login"
        className="login-form form"
        onFinish={onFinish}
        form={form}
      >
        <Form.Item
          name="date"
          rules={[{ required: true, message: "Date is required" }]}
        >
          <DatePicker
            placeholder="Select Resign Date"
            disabledDate={disabledDate}
            style={{
              width: "100%",
            }}
          />
        </Form.Item>
        <Form.Item
          name="reason"
          rules={[{ required: true, message: "This field is required" }]}
        >
          <TextArea rows={4} placeholder="Reason" />
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

export default ApplyResignation;
