//! Ant Imports

import { Modal } from "antd";
import ReceiptPDF from "./ReceiptPDF";

function FDReceiptModal({ record, visible, onCreate, onCancel }) {
  const title = <span className="cb-text-strong">Fd Receipt</span>;
  return (
    <Modal
      visible={visible}
      title={title}
      okText="Download"
      cancelText="Cancel"
      onCancel={onCancel}
      onOk={onCreate}
      footer={null}
      centered
      keyboard={false}
      maskClosable={false}
    >
      <ReceiptPDF record={record} />
    </Modal>
  );
}

export default FDReceiptModal;
