//! Ant Imports

import { Modal } from "antd";

function AccountRejectModal({ record, modalState, onCreate, onCancel }) {
  const { visible, confirmLoading } = modalState;

  return (
    <Modal
      visible={visible}
      title={`Reject customer request: ${record.firstName} ${record.lastName}`}
      okText="Reject"
      cancelText="Cancel"
      onCancel={onCancel}
      onOk={onCreate}
      centered
      keyboard={false}
      maskClosable={false}
      confirmLoading={confirmLoading}
    >
      Are you sure, you want to reject this request?
    </Modal>
  );
}

export default AccountRejectModal;
