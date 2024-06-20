import {
  Animated,
  FlatList,
  Pressable,
  StyleSheet,
  Text,
  View,
} from "react-native";
import { useNavigation } from "@react-navigation/native";
import { useDebugValue, useEffect, useState } from "react";
import { Ionicons } from "@expo/vector-icons";
import {
  getAlertList,
  readAllAlert,
  deleteAlert,
  deleteAllAlert,
} from "../../utils/AlertApi";

import StackHeader from "../../components/common/StackHeader";
import Colors from "../../constants/Colors";
import {
  GestureHandlerRootView,
  Swipeable,
} from "react-native-gesture-handler";
import { useDispatch } from "react-redux";
import { setIsAlert } from "../../stores/alertSlice";

const AlertListScreen = () => {
  const Navigation = useNavigation();
  const [alerts, setalerts] = useState([]);
  const dispatch = useDispatch()

  const getList = async () => {
    const res = await getAlertList();
    //console.log(res.alert);
    setalerts(res.alerts); // 상태 업데이트
  };

  const renderRightActions = (dragX, code) => {
    // const trans = dragX.interpolate({
    //   inputRange: [0, 50, 100, 101],
    //   outputRange: [-20, 0, 0, 1],
    // });
    return (
      <Pressable
        style={styles.swiped}
        onPress={() => {
          deleteAlert(code);
          getList();
        }}
      >
        <Animated.Text
          style={[
            styles.delete,
            // {
            //   transform: [{ translateX: trans }],
            // },
          ]}
        >
          삭제
        </Animated.Text>
      </Pressable>
    );
  };

  useEffect(() => {
    getList();
  }, []);

  const renderAlertList = (itemData) => {
    const date = itemData.item.createTime.split("T")[0];

    return (
      <GestureHandlerRootView>
        <Swipeable
          renderRightActions={(dragX) =>
            renderRightActions(dragX, itemData.item.alertCode)
          }
        >
          <View style={styles.container}>
            <View>
              <Text
                style={
                  itemData.item.isRead == 0 ? styles.title : styles.readTitle
                }
              >
                {itemData.item.title}
              </Text>
              <Text style={styles.date}>
                {date.split("-")[0]}년 {date.split("-")[1]}월{" "}
                {date.split("-")[2]}일
              </Text>
            </View>
            <View>
              <Ionicons
                name="chevron-forward"
                size={24}
                color="black"
                onPress={() => {
                  Navigation.navigate("alert", {
                    alertCode: itemData.item.alertCode,
                  });
                  const readCheck = alerts.map((item) => {
                    //console.log;
                    if (item.alertCode === itemData.item.alertCode) {
                      return { ...item, isRead: true };
                    }
                    return item;
                  });
                  setalerts(readCheck);
                }}
              />
            </View>
          </View>
        </Swipeable>
      </GestureHandlerRootView>
    );
  };

  return (
    <>
      <StackHeader screenName="home" />
      <View style={styles.screen}>
        <View style={styles.headerContainer}>
          <Text style={styles.header}>알림</Text>
          <View style={styles.buttonsContainer}>
            <Text
              onPress={() => {
                readAllAlert();
                getList();
                dispatch(setIsAlert(false))
              }}
            >
              모두 읽음
            </Text>
            <Text
              onPress={() => {
                deleteAllAlert();
                getList();
              }}
              style={{ marginLeft: 15 }}
            >
              모두 삭제
            </Text>
          </View>
        </View>
        <FlatList
          data={alerts}
          keyExtractor={(item, index) => index.toString()}
          renderItem={renderAlertList}
        />
      </View>
    </>
  );
};

export default AlertListScreen;

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
  readTitle: {
    fontFamily: "pretendard-regular",
    fontSize: 15,
  },
  date: {
    fontFamily: "pretendard-regular",
    fontSize: 13,
    color: Colors.fontGray,
  },
  delete: {
    color: "white",
  },
  swiped: {
    alignItems: "center",
    justifyContent: "center",
    width: 70,
    backgroundColor: "red",
  },
  headerContainer: {
    flexDirection: "row",
    alignItems: "center",
    justifyContent: "space-between",
  },
  buttonsContainer: {
    marginRight: 10,
    flexDirection: "row",
  },
});
