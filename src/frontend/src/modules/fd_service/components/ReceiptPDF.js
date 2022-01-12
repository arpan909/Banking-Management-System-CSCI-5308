import {
  Document,
  Page,
  Text,
  View,
  PDFDownloadLink,
} from "@react-pdf/renderer";
import { styles } from "common/fdPDFStyles";
import moment from "moment";

const PDF = ({ record }) => {
  const {
    termDepositId,
    startDate,
    maturityDate,
    account,
    initialAmount,
    duration,
    maturityAmount,
    rateOfInterest,
    termDepositStatus,
  } = record;
  const {
    user: { id, firstName, lastName },
    accountNumber,
    balance,
    creditScore,
  } = account;
  return (
    <Document>
      <Page style={styles.body}>
        <View>
          <Text style={styles.header} fixed>
            Dalhousie Group 2 5308 Bank
          </Text>
        </View>
        <View>
          <Text style={styles.title}>Fix Deposit Receipt</Text>
        </View>
        <View>
          <Text style={styles.author}>Customer ID - DAL{id}</Text>
        </View>
        <View style={styles.body}>
          <View style={styles.row}>
            <Text style={{ ...styles.text, ...styles.left }}>
              <Text style={styles.textBold}>FD Number: </Text>
              {termDepositId}
            </Text>
            <Text style={{ ...styles.text, ...styles.right }}>
              <Text style={styles.textBold}>Customer Name: </Text>
              {firstName} {lastName}
            </Text>
          </View>
          <View style={styles.row}>
            <Text style={{ ...styles.text, ...styles.left }}>
              <Text style={styles.textBold}>Start Date: </Text>
              {moment(startDate).format("Do MMM YYYY")}
            </Text>
            <Text style={{ ...styles.text, ...styles.right }}>
              <Text style={styles.textBold}>Maturity Date: </Text>
              {moment(maturityDate).format("Do MMM YYYY")}
            </Text>
          </View>
          <View style={styles.space}>
            <Text style={styles.author}>
              ------------------------------------------------ ACCOUNT DETAILS
              ------------------------------------------------
            </Text>
            <Text style={styles.text}>
              <Text>Account Number: </Text>
              <Text>{accountNumber}</Text>
            </Text>
            <Text style={styles.text}>
              <Text>Current Balance: </Text>
              <Text>${balance}</Text>
            </Text>
            <Text style={styles.text}>
              <Text>Credit Score: </Text>
              <Text>{creditScore}</Text>
            </Text>
          </View>
          <View style={styles.space}>
            <Text style={styles.author}>
              ----------------------------------------------------- FD DETAILS
              -----------------------------------------------------
            </Text>
            <View style={styles.row}>
              <Text style={{ ...styles.text, ...styles.left }}>
                <Text>Initial Amount: </Text>
                <Text>${initialAmount}</Text>
              </Text>
              <Text style={{ ...styles.text, ...styles.right }}>
                <Text>Maturity Amount: </Text>
                <Text>${maturityAmount.toFixed(2)}</Text>
              </Text>
            </View>
            <View style={styles.row}>
              <Text style={{ ...styles.text, ...styles.left }}>
                <Text>Duration: </Text>
                <Text>{duration} Years</Text>
              </Text>
              <Text style={{ ...styles.text, ...styles.right }}>
                <Text>Rate of Interest: </Text>
                <Text>{rateOfInterest}%</Text>
              </Text>
            </View>
            <View style={styles.row}>
              <Text style={{ ...styles.text, ...styles.left }}>
                <Text>Maturity Date: </Text>
                <Text>{moment(maturityDate).format("Do MMM YYYY")}</Text>
              </Text>
              <Text style={{ ...styles.text, ...styles.right }}>
                <Text>FD Status: </Text>
                <Text>{termDepositStatus}</Text>
              </Text>
            </View>
          </View>
          <View style={styles.space}>
            <Text style={styles.center}>
              ------------------------------------------------------------------
            </Text>
            <Text style={styles.center}>
              Generated at: {moment().format("LLLL")}
            </Text>
            <Text style={styles.center}>
              ------------------------------------------------------------------
            </Text>
            <Text style={styles.center}>Happy Banking!</Text>
          </View>
        </View>
      </Page>
    </Document>
  );
};

function ReceiptPDF({ record }) {
  return (
    <PDFDownloadLink
      document={<PDF record={record} />}
      fileName={`fd-receipt-${record.termDepositId}.pdf`}
    >
      {({ blob, url, loading, error }) =>
        loading ? "Preparing Receipt" : "Download Receipt!"
      }
    </PDFDownloadLink>
  );
}

export default ReceiptPDF;
