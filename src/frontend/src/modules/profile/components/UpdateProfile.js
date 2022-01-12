import { useContext, useEffect, useState } from "react";
import moment from "moment";

//! Ant Imports

import {
  Form,
  Input,
  Typography,
  Button,
  Row,
  Col,
  Select,
  DatePicker,
} from "antd";

//! Ant Icons

import { MailOutlined, UserOutlined } from "@ant-design/icons";

//! User Files

import * as ActionTypes from "common/actionTypes";
import { AppContext } from "AppContext";
import api from "common/api";
import { toast } from "common/utils";
import { REGEX } from "common/constants";
import ServerError from "components/ServerError";
import Loading from "components/Loading";

const { Option } = Select;
const { Title } = Typography;

function UpdateProfile() {
  const {
    state: { currentUser, authToken },
    dispatch,
  } = useContext(AppContext);

  const date = new Date();
  const eligibleYear = date.getFullYear() - 1;

  const [form] = Form.useForm();

  const [loading, setLoading] = useState(false);
  const [updateLoading, setUpdateLoading] = useState(false);
  const [err, setErr] = useState(false);
  const [userData, setUserData] = useState({});

  const bankUsername = currentUser?.username;

  const username = userData?.username;
  const firstName = userData?.firstName;
  const lastName = userData?.lastName;
  const address = userData?.address;
  const phone = userData?.phone;
  const email = userData?.email;
  const birthday = userData?.birthday;
  const zipCode = userData?.zipCode;
  const city = userData?.city;
  const state = userData?.state;

  const onFinish = async (values) => {
    const {
      firstName,
      lastName,
      birthday,
      prefix,
      phone,
      city,
      state,
      zipCode,
      address,
    } = values;
    let userDetails = {
      firstName,
      lastName,
      phone: `${prefix}${phone}`,
      birthday: moment(birthday).format("YYYY-MM-DD"),
      city,
      state,
      zipCode,
      address,
    };
    setUpdateLoading(true);
    try {
      const response = await api.put("/user/me", userDetails);
      const { data } = response;
      const updatedUserData = {
        ...currentUser,
        ...userDetails,
      };
      dispatch({ type: ActionTypes.SET_CURRENT_USER, data: updatedUserData });
      toast({
        message: data.message,
        type: "success",
      });
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
      setUpdateLoading(false);
    }
  };

  const fetchUserProfile = async () => {
    setLoading(true);
    try {
      const response = await api.get(`/users/${bankUsername}`, {
        headers: {
          Authorization: `Bearer ${authToken}`,
        },
      });
      const { data } = response;
      setUserData(data);
    } catch (err) {
      setErr(true);
      toast({
        message: "Something went wrong!",
        type: "error",
      });
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    form.resetFields();
    // eslint-disable-next-line
  }, [loading]);

  useEffect(() => {
    fetchUserProfile();
    // eslint-disable-next-line
  }, [currentUser]);

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

  if (loading) return <Loading />;
  if (err) return <ServerError />;
  return (
    <Row className="update-profile">
      <Title level={4}>PROFILE</Title>
      <Form
        name="normal_signup"
        className="signup-form form"
        initialValues={{
          prefix: "+91",
          firstName,
          lastName,
          username,
          email,
          birthday: moment(birthday, "YYYY-MM-DD"),
          phone: phone?.substring(phone?.length - 10),
          city,
          state,
          zipCode,
          address,
        }}
        form={form}
        onFinish={onFinish}
      >
        <Row gutter={16}>
          <Col span={12}>
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
          </Col>
          <Col span={12}>
            <Form.Item
              name="lastName"
              rules={[
                { required: true, message: "Last name is required" },
                { min: 3, message: "Last name contains at least 3 characters" },
                {
                  max: 15,
                  message: "Last name contains at most 15 characters",
                },
              ]}
            >
              <Input placeholder="Last Name" />
            </Form.Item>
          </Col>
        </Row>
        <Row gutter={16}>
          <Col span={24}>
            <Form.Item
              name="username"
              rules={[
                { required: true, message: "Username is required" },
                { min: 3, message: "Username contains at least 3 characters" },
                { max: 10, message: "Username contains at most 15 characters" },
              ]}
            >
              <Input
                prefix={<UserOutlined className="site-form-item-icon" />}
                placeholder="Username"
                readOnly
                disabled
              />
            </Form.Item>
          </Col>
        </Row>
        <Row gutter={16}>
          <Col span={24}>
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
                readOnly
                disabled
              />
            </Form.Item>
          </Col>
        </Row>
        <Row gutter={16}>
          <Col span={12}>
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
          </Col>
          <Col span={12}>
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
          </Col>
        </Row>
        <Row gutter={16}>
          <Col span={24}>
            <Form.Item
              name="city"
              rules={[
                { required: true, message: "City name is required" },
                {
                  min: 3,
                  message: "City name at contains at least 3 characters",
                },
                {
                  max: 50,
                  message: "City name contains at most 50 characters",
                },
              ]}
            >
              <Input placeholder="City" />
            </Form.Item>
          </Col>
        </Row>
        <Row gutter={16}>
          <Col span={12}>
            <Form.Item
              name="state"
              rules={[
                { required: true, message: "State name is required" },
                {
                  min: 3,
                  message: "State name contains at least 3 characters",
                },
                {
                  max: 50,
                  message: "State name contains at most 50 characters",
                },
              ]}
            >
              <Input placeholder="State" />
            </Form.Item>
          </Col>
          <Col span={12}>
            <Form.Item
              name="zipCode"
              rules={[
                { required: true, message: "Zip code is required" },
                { pattern: REGEX.ZIPCODE, message: "Enter valid zip code" },
              ]}
            >
              <Input placeholder="Zip Code" />
            </Form.Item>
          </Col>
        </Row>
        <Row gutter={16}>
          <Col span={24}>
            <Form.Item
              name="address"
              rules={[{ required: true, message: "Address name is required" }]}
            >
              <Input placeholder="Address" />
            </Form.Item>
          </Col>
        </Row>
        <Row gutter={16}>
          <Col span={24}>
            <Form.Item>
              <Button
                loading={updateLoading}
                type="primary"
                htmlType="submit"
                className="signup-form-button button"
              >
                Update Profile
              </Button>
            </Form.Item>
          </Col>
        </Row>
      </Form>
    </Row>
  );
}

export default UpdateProfile;
