import { useEffect, useState } from "react";
import { FlatList, ScrollView, StyleSheet, Text, View } from "react-native";
import { getNoticeList } from "../../utils/noticeAPi";
import { Ionicons } from "@expo/vector-icons";
import { useNavigation } from "@react-navigation/native";

import StackHeader from "../../components/common/StackHeader";
import Colors from "../../constants/Colors";

const AnnouncementScreen = () => {
  const Navigation = useNavigation();
  const [announcements, setAnnouncements] = useState([]);

  useEffect(() => {
    const getList = async () => {
      const res = await getNoticeList();
      setAnnouncements(res.notices); // 상태 업데이트
    };
    getList();
  }, []);

  const renderAnnouncementList = (itemData) => {
    const date = itemData.item.createTime.split("T")[0];

    return (
      <View style={styles.container}>
        <View>
          <Text style={styles.title}>{itemData.item.title}</Text>
          <Text style={styles.date}>
            {date.split("-")[0]}년 {date.split("-")[1]}월 {date.split("-")[2]}일
          </Text>
        </View>
        <View>
          <Ionicons
            name="chevron-forward"
            size={24}
            color="black"
            onPress={() =>
              Navigation.navigate("notice", {
                noticeCode: itemData.item.noticeCode,
              })
            }
          />
        </View>
      </View>
    );
  };

  return (
    <>
      <StackHeader screenName="home" />
      <View style={styles.screen}>
        <Text style={styles.header}>공지사항</Text>
        <FlatList
          data={announcements}
          keyExtractor={(item, index) => index.toString()}
          renderItem={renderAnnouncementList}
        />
      </View>
    </>
  );
};

export default AnnouncementScreen;

const styles = StyleSheet.create({
  screen: {
    flex: 1,
    backgroundColor: "white",
    paddingHorizontal: 25,
  },
  header: {
    fontFamily: "pretendard-extraBold",
    fontSize: 23,
    paddingHorizontal: 10,
    paddingBottom: 20,
  },
  container: {
    flex: 1,
    marginVertical: 10,
    flexDirection: "row",
    justifyContent: "space-between",
  },
  title: {
    fontFamily: "pretendard-bold",
    fontSize: 15,
  },
  date: {
    fontFamily: "pretendard-regular",
    fontSize: 13,
    color: Colors.fontGray,
  },
});
