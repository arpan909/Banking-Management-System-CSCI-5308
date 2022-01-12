//! Ant Imports

import { Modal } from "antd";

function CreditCardRejectModal({ record, modalState, onCreate, onCancel }) {
  const { visible, confirmLoading } = modalState;
  const { firstName, lastName } = record.accountDetailResponse.userMetaResponse;

  return (
    <Modal
      visible={visible}
      title={`Reject customer request: ${firstName} ${lastName}`}
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

export default CreditCardRejectModal;
