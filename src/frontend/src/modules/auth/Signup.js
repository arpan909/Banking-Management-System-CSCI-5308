import { useContext, useEffect, useState } from "react";
import { Link, useHistory } from "react-router-dom";
import moment from "moment";

//! Ant Imports

import { Form, Input, Button, Typography, Select, DatePicker } from "antd";

//! Ant Icons

import { MailOutlined, LockOutlined, UserOutlined } from "@ant-design/icons";

//! User Files

import { toast } from "common/utils";
import { AppContext } from "AppContext";
import { APP_NAME, REGEX, ROLES, ROUTES } from "common/constants";
import api from "common/api";

const { Title } = Typography;
const { Option } = Select;

function Signup({ isOpenedByManager }) {
  const date = new Date();
  const eligibleYear = date.getFullYear() - 1;
  const {
    state: { authenticated },
  } = useContext(AppContext);
  const [form] = Form.useForm();
  const [visible, setVisible] = useState(false);
  const [loading, setLoading] = useState(false);
  const { push } = useHistory();
  const onFinish = async (values) => {
    const {
      firstName,
      lastName,
      username,
      email,
      birthday,
      prefix,
      phone,
      city,
      state,
      zipCode,
      address,
      role,
      requestedAccountType,
      password,
    } = values;
    setLoading(true);
    let userDetails = {
      firstName,
      lastName,
      username,
      phone: `${prefix}${phone}`,
      email,
      birthday: moment(birthday).format("YYYY-MM-DD"),
      city,
      state,
      zipCode,
      address,
      password,
    };
    try {
      if (isOpenedByManager) {
        userDetails.role = role;
        if (role === ROLES.ROLE_USER) {
          userDetails.requestedAccountType = requestedAccountType;
        }
        userDetails.requestedAccountType = "CURRENT";
      } else {
        userDetails.requestedAccountType = requestedAccountType;
      }
      const response = await api.post(
        isOpenedByManager ? "/users/create" : "/auth/signup",
        userDetails
      );
      const { data } = response;
      if (data?.success) {
        toast({
          message: data.message,
          type: "success",
        });
        !isOpenedByManager ? push(ROUTES.LOGIN) : form.resetFields();
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

  useEffect(() => {
    if (!isOpenedByManager && authenticated) {
      push("/");
    }
    // eslint-disable-next-line
  }, [authenticated]);

  const handleFormValuesChange = (e) => {
    e.role && setVisible(e.role === ROLES.ROLE_USER);
  };

  const disabledDate = (current) => {
    let customDate = `${eligibleYear}-01-01`;
    return current && current > moment(customDate, "YYYY-MM-DD");
  };

  const prefixSelector = (
    <Form.Item name="prefix" noStyle>
      <Select style={{ width: 70 }}>
        <Option value="91">+91</Option>
        <Option value="1">+1</Option>
      </Select>
    </Form.Item>
  );

  return (
    <div className="login">
      <Title level={3} className="sdp-text-strong">
        {isOpenedByManager ? "Create User" : APP_NAME}
      </Title>
      <Form
        name="normal_signup"
        className="signup-form form"
        initialValues={{ prefix: "+91" }}
        form={form}
        onValuesChange={handleFormValuesChange}
        onFinish={onFinish}
      >
        <Form.Item
          name="firstName"
          rules={[
            { required: true, message: "First name is required" },
            {
              min: 3,
              message: "First name contains at least 3 characters",
            },
            {
              max: 15,
              message: "First name contains at most 15 characters",
            },
          ]}
        >
          <Input placeholder="First Name" />
        </Form.Item>
        <Form.Item
          name="lastName"
          rules={[
            { required: true, message: "Last name is required" },
            { min: 3, message: "Last name contains at least 3 characters" },
            { max: 15, message: "Last name contains at most 15 characters" },
          ]}
        >
          <Input placeholder="Last Name" />
        </Form.Item>
        <Form.Item
          name="username"
          rules={[
            { required: true, message: "Username is required" },
            { min: 3, message: "Username contains at least 3 characters" },
            { max: 10, message: "Username contains at most 10 characters" },
          ]}
        >
          <Input
            prefix={<UserOutlined className="site-form-item-icon" />}
            placeholder="Username"
          />
        </Form.Item>
        <Form.Item
          name="email"
          rules={[
            {
              type: "email",
              message: "The input is not valid a email!",
            },
            { required: true, message: "Email is required" },
          ]}
        >
          <Input
            prefix={<MailOutlined className="site-form-item-icon" />}
            placeholder="Email"
          />
        </Form.Item>
        <Form.Item
          name="birthday"
          rules={[{ required: true, message: "Birthday is required" }]}
        >
          <DatePicker
            placeholder="Select Birthday"
            disabledDate={disabledDate}
            style={{
              width: "100%",
            }}
          />
        </Form.Item>
        <Form.Item
          name="phone"
          rules={[
            { required: true, message: "Phone number is required!" },
            {
              pattern: REGEX.PHONE,
              message: "Enter valid phone number",
            },
          ]}
        >
          <Input
            placeholder="Phone Number"
            addonBefore={prefixSelector}
            style={{ width: "100%" }}
          />
        </Form.Item>
        <Form.Item
          name="city"
          rules={[
            { required: true, message: "City name is required" },
            { min: 3, message: "City name at contains at least 3 characters" },
            { max: 50, message: "City name contains at most 50 characters" },
          ]}
        >
          <Input placeholder="City" />
        </Form.Item>
        <Form.Item
          name="state"
          rules={[
            { required: true, message: "State name is required" },
            { min: 3, message: "State name contains at least 3 characters" },
            { max: 50, message: "State name contains at most 50 characters" },
          ]}
        >
          <Input placeholder="State" />
        </Form.Item>
        <Form.Item
          name="zipCode"
          rules={[
            { required: true, message: "Zip code is required" },
            { pattern: REGEX.ZIPCODE, message: "Enter valid zip code" },
          ]}
        >
          <Input placeholder="Zip Code" />
        </Form.Item>
        <Form.Item
          name="address"
          rules={[{ required: true, message: "Address name is required" }]}
        >
          <Input placeholder="Address" />
        </Form.Item>
        {isOpenedByManager && (
          <Form.Item
            name="role"
            rules={[{ required: true, message: "User role is required" }]}
          >
            <Select allowClear placeholder="User Role">
              <Option value={ROLES.ROLE_USER}>Customer</Option>
              <Option value={ROLES.ROLE_MANAGER}>Manager</Option>
              <Option value={ROLES.ROLE_EMPLOYEE}>Employee</Option>
              <Option value={ROLES.ROLE_HR}>HR</Option>
            </Select>
          </Form.Item>
        )}
        {isOpenedByManager ? (
          visible && (
            <Form.Item
              name="requestedAccountType"
              rules={[{ required: true, message: "Account Type is required" }]}
            >
              <Select allowClear placeholder="Account Type">
                <Option value="SAVINGS">Savings</Option>
                <Option value="CURRENT">Current</Option>
              </Select>
            </Form.Item>
          )
        ) : (
          <Form.Item
            name="requestedAccountType"
            rules={[{ required: true, message: "Account Type is required" }]}
          >
            <Select allowClear placeholder="Account Type">
              <Option value="SAVINGS">Savings</Option>
              <Option value="CURRENT">Current</Option>
            </Select>
          </Form.Item>
        )}
        <Form.Item
          name="password"
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
            placeholder="Password"
          />
        </Form.Item>
        <Form.Item
          name="confirm"
          dependencies={["password"]}
          rules={[
            {
              required: true,
              message: "Please confirm your password!",
            },
            ({ getFieldValue }) => ({
              validator(_, value) {
                if (!value || getFieldValue("password") === value) {
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
            placeholder="Confirm Password"
          />
        </Form.Item>
        <Form.Item>
          <Button
            loading={loading}
            type="primary"
            htmlType="submit"
            className="signup-form-button button"
          >
            {isOpenedByManager ? "Create User" : "Register"}
          </Button>
          {!isOpenedByManager && (
            <div className="reg-user-actions">
              <Link to={ROUTES.LOGIN}>Already a user!</Link>
            </div>
          )}
        </Form.Item>
      </Form>
    </div>
  );
}

export default Signup;
