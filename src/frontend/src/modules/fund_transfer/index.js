import { useContext, useEffect, useState } from "react";

//! Ant Imports

import { Form, Input, Button, Typography } from "antd";

//! User Files

import { toast } from "common/utils";
import { REGEX } from "common/constants";
import api from "common/api";
import Loading from "components/Loading";
import TextArea from "antd/lib/input/TextArea";
import ServerError from "components/ServerError";
import { AppContext } from "AppContext";

const { Title } = Typography;

function FundTransfer() {
  const {
    state: { authToken },
  } = useContext(AppContext);
  const [loading, setLoading] = useState(false);
  const [formLoading, setFormLoading] = useState(false);
  const [err, setErr] = useState(false);
  const [accountData, setAccountData] = useState({});

  const [form] = Form.useForm();

  const senderAccountNumber = accountData?.accountNumber;

  const onFinish = async (values) => {
    if (values.senderAccountNumber === Number(values.receiverAccountNumber)) {
      toast({
        message: "Sender and Receiver must be different",
        type: "info",
      });
    } else {
      setLoading(true);
      try {
        const response = await api.post("/account/activity", values);
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
      }
      setLoading(false);
    }
  };

  const fetchUserAccount = async () => {
    setLoading(true);
    setFormLoading(true);
    try {
      const response = await api.get(`/account/me`, {
        headers: {
          Authorization: `Bearer ${authToken}`,
        },
      });
      const { data } = response;
      setAccountData(data);
    } catch (err) {
      setErr(true);
      toast({
        message: "Something went wrong!",
        type: "error",
      });
    } finally {
      setLoading(false);
      setFormLoading(false);
    }
  };

  useEffect(() => {
    form.resetFields();
    // eslint-disable-next-line
  }, [formLoading]);

  useEffect(() => {
    fetchUserAccount();
    // eslint-disable-next-line
  }, []);

  if (formLoading) return <Loading />;
  if (err) return <ServerError />;
  return (
    <div className="login">
      <Title level={3} className="sdp-text-strong">
        Fund Transfer
      </Title>
      <Form
        form={form}
        name="normal_login"
        className="login-form form"
        onFinish={onFinish}
        initialValues={{
          senderAccountNumber,
        }}
      >
        <Form.Item
          name="senderAccountNumber"
          rules={[
            { required: true, message: "This field is required" },
            { pattern: REGEX.NUMBER, message: "Only digits are allowed" },
          ]}
        >
          <Input readOnly placeholder="Sender Account Number" />
        </Form.Item>
        <Form.Item
          name="receiverAccountNumber"
          rules={[
            { required: true, message: "This field is required" },
            { pattern: REGEX.NUMBER, message: "Only digits are allowed" },
          ]}
        >
          <Input.Password
            type="password"
            placeholder="Receiver Account Number"
          />
        </Form.Item>
        <Form.Item
          name="confirm"
          dependencies={["receiverAccountNumber"]}
          rules={[
            {
              required: true,
              message: "Please confirm account number!",
            },
            ({ getFieldValue }) => ({
              validator(_, value) {
                if (
                  !value ||
                  getFieldValue("receiverAccountNumber") === value
                ) {
                  return Promise.resolve();
                }
                return Promise.reject(
                  new Error(
                    "The two account numbers that you entered do not match!"
                  )
                );
              },
            }),
          ]}
        >
          <Input.Password
            type="password"
            placeholder="Confirm Receiver Account Number"
          />
        </Form.Item>
        <Form.Item
          name="transactionAmount"
          rules={[
            { required: true, message: "This field is required" },
            { pattern: REGEX.NUMBER, message: "Only digits are allowed" },
          ]}
        >
          <Input placeholder="Amount" />
        </Form.Item>
        <Form.Item name="comment">
          <TextArea rows={4} placeholder="Comment (Optional)" />
        </Form.Item>
        <Form.Item>
          <Button
            loading={loading}
            type="primary"
            htmlType="submit"
            className="login-form-button button"
          >
            Transfer Funds
          </Button>
        </Form.Item>
      </Form>
    </div>
  );
}

export default FundTransfer;
