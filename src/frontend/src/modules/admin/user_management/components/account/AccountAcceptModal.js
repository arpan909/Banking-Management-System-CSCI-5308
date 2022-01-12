//! Ant Imports

import { Modal, Form, Input } from "antd";

function AccountAcceptModal({ record, modalState, onCreate, onCancel, form }) {
  const { visible, confirmLoading } = modalState;

  return (
    <Modal
      visible={visible}
      title={`Approve customer request: ${record.firstName} ${record.lastName}`}
      okText="Approve"
      cancelText="Cancel"
      onCancel={onCancel}
      onOk={onCreate}
      centered
      keyboard={false}
      maskClosable={false}
      confirmLoading={confirmLoading}
    >
      <Form
        form={form}
        layout="vertical"
        name="form_in_modal"
        initialValues={{ modifier: "public" }}
      >
        <Form.Item
          name="balance"
          type="number"
          rules={[
            {
              required: true,
              message: "This field is required!",
            },
          ]}
        >
          <Input placeholder="Balance" />
        </Form.Item>
        <Form.Item
          name="creditScore"
          type="number"
          rules={[
            {
              required: true,
              message: "This field is required!",
            },
          ]}
        >
          <Input placeholder="Credit Score" />
        </Form.Item>
      </Form>
    </Modal>
  );
}

export default AccountAcceptModal;
