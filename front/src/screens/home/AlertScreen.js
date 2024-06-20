import { StyleSheet, Text, View } from "react-native";
import StackHeader from "../../components/common/StackHeader";
import { useEffect, useState } from "react";
import Colors from "../../constants/Colors";
import { getAlertDetail } from "../../utils/AlertApi";

const AlertScreen = ({ route }) => {
  const [alert, setAlert] = useState({
    title: "",
    createTime: "",
    content: "",
    isRead: false,
  });

  useEffect(() => {
    const getDetail = async () => {
      const res = await getAlertDetail(route.params.alertCode);
      setAlert(res);
    };
    getDetail();
  }, []);

  return (
    <>
      <StackHeader screenName="alertList" />
      <View style={styles.screen}>
        <Text style={styles.title}>{alert.title}</Text>
        <Text style={styles.date}>
          {alert.createTime.split("T")[0].split("-")[0]}년{" "}
          {alert.createTime.split("T")[0].split("-")[1]}월{" "}
          {alert.createTime.split("T")[0].split("-")[2]}일
        </Text>
        <Text style={styles.content}>{alert.content}</Text>
      </View>
    </>
  );
};

export default AlertScreen;

const styles = StyleSheet.create({
  screen: {
    flex: 1,
    backgroundColor: "white",
    paddingHorizontal: 25,
  },
  title: {
    fontFamily: "pretendard-extraBold",
    fontSize: 23,
  },
  date: {
    fontFamily: "pretendard-regular",
    fontSize: 13,
    color: Colors.fontGray,
    marginTop: 10,
  },
  content: {
    fontFamily: "pretendard-regular",
    fontSize: 15,
    marginTop: 30,
  },
});
