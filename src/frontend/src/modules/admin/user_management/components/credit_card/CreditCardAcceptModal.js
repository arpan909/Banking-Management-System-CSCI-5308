//! Ant Imports

import { Modal } from "antd";

function CreditCardAcceptModal({ record, modalState, onCreate, onCancel }) {
  const { visible, confirmLoading } = modalState;
  const { firstName, lastName } = record.accountDetailResponse.userMetaResponse;
  return (
    <Modal
      visible={visible}
      title={`Approve customer request: ${firstName} ${lastName}`}
      okText="Approve"
      cancelText="Cancel"
      onCancel={onCancel}
      onOk={onCreate}
      centered
      keyboard={false}
      maskClosable={false}
      confirmLoading={confirmLoading}
    >
      Are you sure, you want to approve this request?
    </Modal>
  );
}

export default CreditCardAcceptModal;
