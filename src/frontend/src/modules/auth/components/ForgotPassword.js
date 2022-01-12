import { useContext, useEffect, useState } from "react";
import { Link, useHistory } from "react-router-dom";

//! Ant Imports

import { Form, Input, Button, Typography } from "antd";

//! Ant Icons

import { MailOutlined } from "@ant-design/icons";

//! User Files

import { toast } from "common/utils";
import { AppContext } from "AppContext";
import { ROUTES } from "common/constants";
import api from "common/api";

const { Title } = Typography;

function Login() {
  const {
    state: { authenticated },
  } = useContext(AppContext);
  const [loading, setLoading] = useState(false);
  const { push } = useHistory();
  const onFinish = async (values) => {
    setLoading(true);
    try {
      const response = await api.post("/forgot-password", values);
      const { data } = response;
      if (data?.success) {
        toast({
          message: data.message,
          type: "success",
        });
      }
    } catch (err) {
      toast({
        message: "Something went wrong. Please try again!",
        type: "error",
      });
    }
    setLoading(false);
  };

  useEffect(() => {
    if (authenticated) {
      push("/");
    }
    // eslint-disable-next-line
  }, [authenticated]);

  return (
    <div className="login">
      <Title level={3} className="sdp-text-strong">
        Reset Password
      </Title>
      <Form
        name="normal_forgot_password"
        className="forgot-password-form form"
        onFinish={onFinish}
      >
        <Form.Item
          name="email"
          rules={[
            { required: true, message: "This field is required" },
            {
              type: "email",
              message: "Entered email is not a valid email",
            },
          ]}
        >
          <Input
            prefix={<MailOutlined className="site-form-item-icon" />}
            placeholder="Enter your email"
          />
        </Form.Item>
        <Form.Item>
          <Button
            loading={loading}
            type="primary"
            htmlType="submit"
            className="forgot-password-form-button button"
          >
            Send Reset Password Link
          </Button>
          <div className="user-actions">
            <div />
            <Link to={ROUTES.LOGIN}>Login now</Link>
          </div>
        </Form.Item>
      </Form>
    </div>
  );
}

export default Login;
