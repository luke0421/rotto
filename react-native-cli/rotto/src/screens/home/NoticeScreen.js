import { StyleSheet, Text, View } from "react-native";
import StackHeader from "../../components/common/StackHeader";
import { useEffect, useState } from "react";
import { getNoticeDetail } from "../../utils/noticeAPi";
import Colors from "../../constants/Colors";

const NoticeScreen = ({ route }) => {
  const [notice, setNotice] = useState({
    title: "",
    createTime: "",
    content: "",
  });

  useEffect(() => {
    const getDetail = async () => {
      const res = await getNoticeDetail(route.params.noticeCode);
      setNotice(res);
    };
    getDetail();
  }, []);

  return (
    <>
      <StackHeader screenName="announcement" />
      <View style={styles.screen}>
        <Text style={styles.title}>{notice.title}</Text>
        <Text style={styles.date}>
          {notice.createTime.split("T")[0].split("-")[0]}년{" "}
          {notice.createTime.split("T")[0].split("-")[1]}월{" "}
          {notice.createTime.split("T")[0].split("-")[2]}일
        </Text>
        <Text style={styles.content}>{notice.content}</Text>
      </View>
    </>
  );
};

export default NoticeScreen;

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
    marginTop: 10
  },
  content: {
    fontFamily: "pretendard-regular",
    fontSize: 15,
    marginTop: 30
  },
});
