import { useContext, useEffect, useState } from "react";
import { Link, useHistory } from "react-router-dom";
import jwtDecode from "jwt-decode";

//! Ant Imports

import { Form, Input, Button, Typography } from "antd";

//! Ant Icons

import { UserOutlined, LockOutlined } from "@ant-design/icons";

//! User Files

import * as ActionTypes from "common/actionTypes";
import { toast } from "common/utils";
import { AppContext } from "AppContext";
import { ACCOUNT_STATUS, APP_NAME, ROUTES } from "common/constants";
import api from "common/api";

const { Title } = Typography;

function Login() {
  const {
    state: { authenticated },
    dispatch,
  } = useContext(AppContext);
  const [loading, setLoading] = useState(false);
  const { push } = useHistory();
  const onFinish = async (values) => {
    setLoading(true);
    try {
      const response = await api.post("/auth/signin", values);
      const { data } = response;
      if (data?.accessToken) {
        const decoded = jwtDecode(data.accessToken);
        const { sub, role, user, email } = decoded;
        user.email = email;
        if (user.accountStatus === ACCOUNT_STATUS.PENDING) {
          toast({
            message: "Your account has not been activated yet",
            type: "info",
          });
        } else if (user.accountStatus === ACCOUNT_STATUS.REJECTED) {
          toast({
            message: "Sorry! Your account opening request has been rejected",
            type: "info",
          });
        } else if (user.accountStatus === ACCOUNT_STATUS.CLOSED) {
          toast({
            message: "Unable to login! Your account has been closed",
            type: "info",
          });
        } else {
          dispatch({ type: ActionTypes.SET_TOKEN, data: data.accessToken });
          dispatch({ type: ActionTypes.SET_CURRENT_USER, data: user });
          dispatch({ type: ActionTypes.SET_USER_ID, data: sub });
          dispatch({ type: ActionTypes.SET_ROLE, data: role });
          dispatch({ type: ActionTypes.SET_AUTHENTICATED, data: true });
        }
      }
    } catch (err) {
      if (err.response?.data) {
        toast({
          message:
            err.response.data.status === 401
              ? "Please check your credentials"
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
    if (authenticated) {
      push("/");
    }
    // eslint-disable-next-line
  }, [authenticated]);

  return (
    <div className="login">
      <Title level={3} className="sdp-text-strong">
        {APP_NAME}
      </Title>
      <Form name="normal_login" className="login-form form" onFinish={onFinish}>
        <Form.Item
          name="usernameOrEmail"
          rules={[{ required: true, message: "This field is required" }]}
        >
          <Input
            prefix={<UserOutlined className="site-form-item-icon" />}
            placeholder="Enter username or email"
          />
        </Form.Item>
        <Form.Item
          name="password"
          rules={[{ required: true, message: "This field is required" }]}
        >
          <Input.Password
            prefix={<LockOutlined className="site-form-item-icon" />}
            type="password"
            placeholder="Password"
          />
        </Form.Item>
        <Form.Item>
          <Button
            loading={loading}
            type="primary"
            htmlType="submit"
            className="login-form-button button"
          >
            Login
          </Button>
          <div className="user-actions">
            <Link to={ROUTES.FORGET_PASSWORD}>Forget Password?</Link>
            <Link to={ROUTES.REGISTER}>Register Now!</Link>
          </div>
        </Form.Item>
      </Form>
    </div>
  );
}

export default Login;
