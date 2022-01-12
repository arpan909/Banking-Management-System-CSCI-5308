import { useState } from "react";
import { useHistory } from "react-router-dom";

//! Ant Imports

import { Form, Input, Button, Typography } from "antd";

//! Ant Icons

import { LockOutlined } from "@ant-design/icons";

//! User Files

import { toast } from "common/utils";
import { REGEX, ROUTES } from "common/constants";
import api from "common/api";

const { Title } = Typography;

function ChangePassword() {
  const [loading, setLoading] = useState(false);
  const { push } = useHistory();
  const onFinish = async (values) => {
    setLoading(true);
    try {
      const response = await api.post("/users/change-password", values);
      const { data } = response;
      if (data?.success) {
        toast({
          message: data.message,
          type: "success",
        });
        push(ROUTES.LOGOUT);
      } else {
        toast({
          message: data.message,
          type: "error",
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
    }
    setLoading(false);
  };

  return (
    <div className="login change-password">
      <Title level={3} className="sdp-text-strong">
        Change Password
      </Title>
      <Form
        name="normal_reset_password"
        className="reset-password-form form"
        onFinish={onFinish}
      >
        <Form.Item
          name="oldPassword"
          rules={[{ required: true, message: "This field is required" }]}
        >
          <Input.Password
            prefix={<LockOutlined className="site-form-item-icon" />}
            type="password"
            placeholder="Old Password"
          />
        </Form.Item>
        <Form.Item
          name="newPassword"
          rules={[
            { required: true, message: "This field is required" },
            {
              pattern: REGEX.PASSWORD,
              message:
                "Password must contain combination of lowercase, uppercase, special characters",
            },
          ]}
        >
          <Input.Password
            prefix={<LockOutlined className="site-form-item-icon" />}
            type="password"
            placeholder="New Password"
          />
        </Form.Item>
        <Form.Item
          name="confirmNewPassword"
          dependencies={["newPassword"]}
          rules={[
            {
              required: true,
              message: "Please confirm your password!",
            },
            ({ getFieldValue }) => ({
              validator(_, value) {
                if (!value || getFieldValue("newPassword") === value) {
                  return Promise.resolve();
                }
                return Promise.reject(
                  new Error("The two passwords that you entered do not match!")
                );
              },
            }),
          ]}
        >
          <Input.Password
            prefix={<LockOutlined className="site-form-item-icon" />}
            type="password"
            placeholder="Confirm New Password"
          />
        </Form.Item>
        <Form.Item>
          <Button
            loading={loading}
            type="primary"
            htmlType="submit"
            className="reset-password-form-button button"
          >
            Change Password
          </Button>
        </Form.Item>
      </Form>
    </div>
  );
}

export default ChangePassword;
