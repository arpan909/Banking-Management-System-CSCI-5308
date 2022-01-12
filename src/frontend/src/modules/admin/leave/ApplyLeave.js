import { useState } from "react";

//! Ant Imports

import { Form, Input, Button, Typography, DatePicker } from "antd";

//! User Files

import { toast } from "common/utils";
import api from "common/api";
import moment from "moment";

const { Title } = Typography;
const { RangePicker } = DatePicker;
const { TextArea } = Input;

function ApplyLeave() {
  const [loading, setLoading] = useState(false);
  const [form] = Form.useForm();

  const onFinish = async (values) => {
    setLoading(true);
    const { reason, rangePicker } = values;
    const fromDate = moment(rangePicker[0]).format("YYYY-MM-DD");
    const toDate = moment(rangePicker[1]).format("YYYY-MM-DD");
    try {
      const response = await api.post("/staff/leave", {
        reason,
        fromDate,
        toDate,
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

  const rangeConfig = {
    rules: [
      {
        type: "array",
        required: true,
        message: "Please select date!",
      },
    ],
  };

  return (
    <div className="login">
      <Title level={3} className="sdp-text-strong">
        Apply Leave
      </Title>
      <Form
        name="normal_login"
        className="login-form form"
        onFinish={onFinish}
        form={form}
      >
        <Form.Item name="rangePicker" {...rangeConfig}>
          <RangePicker />
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

export default ApplyLeave;
