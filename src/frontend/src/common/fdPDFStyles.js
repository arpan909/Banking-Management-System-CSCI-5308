import { StyleSheet, Font } from "@react-pdf/renderer";

Font.register({
  family: "Oswald",
  src: "https://fonts.gstatic.com/s/oswald/v13/Y_TKV6o8WovbUd3m_X9aAA.ttf",
});

export const styles = StyleSheet.create({
  body: {
    paddingTop: 15,
    paddingBottom: 30,
    paddingHorizontal: 20,
  },
  title: {
    fontSize: 24,
    textAlign: "center",
    fontFamily: "Oswald",
  },
  author: {
    fontSize: 12,
    textAlign: "center",
    marginBottom: 40,
  },
  center: {
    fontSize: 12,
    textAlign: "center",
    padding: 5,
  },
  subtitle: {
    fontSize: 18,
    margin: 12,
    fontFamily: "Oswald",
  },
  text: {
    margin: 12,
    fontSize: 14,
    textAlign: "justify",
    fontFamily: "Oswald",
  },
  textBold: {
    fontWeight: "bold",
  },
  image: {
    marginVertical: 15,
    marginHorizontal: 100,
  },
  header: {
    fontSize: 12,
    marginBottom: 15,
    textAlign: "center",
    color: "grey",
  },
  space: {
    paddingTop: 25,
  },
  row: {
    flex: 1,
    flexDirection: "row",
    flexGrow: 1,
  },
  left: {
    flexGrow: 0,
    flexShrink: 1,
    flexBasis: 200,
  },
  right: {
    textAlign: "right",
    flexShrink: 1,
    flexGrow: 2,
  },
  pageNumber: {
    position: "absolute",
    fontSize: 12,
    bottom: 30,
    left: 0,
    right: 0,
    textAlign: "center",
    color: "grey",
  },
});
