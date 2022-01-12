import { useContext, useEffect, useState } from "react";
import { Link, useHistory } from "react-router-dom";

//! Ant Imports

import { Form, Input, Button, Typography } from "antd";

//! Ant Icons

import { LockOutlined } from "@ant-design/icons";

//! User Files

import { toast, useRouteQuery } from "common/utils";
import { AppContext } from "AppContext";
import { REGEX, ROUTES } from "common/constants";
import api from "common/api";

const { Title } = Typography;

function ResetPassword() {
  const {
    state: { authenticated },
  } = useContext(AppContext);
  const query = useRouteQuery();
  const [token, setToken] = useState();
  const [loading, setLoading] = useState(false);
  const { push } = useHistory();
  const onFinish = async (values) => {
    setLoading(true);
    try {
      const resetData = {
        token,
        ...values,
      };
      const response = await api.post("/reset-password", resetData);
      const { data } = response;
      if (data?.success) {
        toast({
          message: data.message,
          type: "success",
        });
        push(ROUTES.LOGIN);
      }
    } catch (err) {
      if (err.response?.data) {
        toast({
          message:
            err.response.data.status === 404
              ? "Reset token does not exist or it is expired"
              : err.response.data.message,
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

  useEffect(() => {
    setToken(query.get("token"));
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
        name="normal_reset_password"
        className="reset-password-form form"
        onFinish={onFinish}
      >
        <Form.Item
          name="newPassword"
          rules={[
            { required: true, message: "Please enter your password!" },
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
          <div className="reg-user-actions">
            <Link to={ROUTES.LOGIN}>Login now</Link>
          </div>
        </Form.Item>
      </Form>
    </div>
  );
}

export default ResetPassword;
