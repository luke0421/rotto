import React, { useEffect, useState, useRef } from "react";
import {
  StyleSheet,
  View,
  Dimensions,
  Image,
  Pressable
} from "react-native";
import { useNavigation } from "@react-navigation/native";
import { Text } from "react-native-paper";
import { getFarmILikeList } from "../../utils/FarmILikeApi";
import { Ionicons } from '@expo/vector-icons';

const { width } = Dimensions.get("window");

const FarmILike = () => {
  const [farmILikeList, setfarmILikeList] = useState([]);

  const getList = async () => {
    const res = await getFarmILikeList();
    //console.log(res.data.farms);
    setfarmILikeList(res.data.farms);
  };

  useEffect(() => {
    getList();
  }, []);
  const Navigation = useNavigation();
  const [currentIndex, setCurrentIndex] = useState(0);

  return (
    <View style={styles.screen}>
      {farmILikeList ? <View>
        <View style={styles.headContainer}>
          <Text
            style={styles.header}
          >관심 농장</Text>
          <Pressable
          onPress={() => Navigation.navigate("farmILikeScreen")}
          >
            <Text
              style={styles.editBtnStyle}
            >
              편집하기
            </Text>
          </Pressable>
        </View>
        <View style={styles.container}>
          {farmILikeList.map((farm, index) => index < 3 && (
            <Pressable key={farm.farmCode} style={styles.itemStyle}>
              <View style={styles.itemContentStyle}>
                <Image
                  source={require("../../../assets/images/farmfarm.png")}
                  style={{ width: 60, height: 60 }}
                />
                <View
                  style={{ marginLeft: 10 }}
                >
                  <Text style={styles.content}>
                    {farm.farmName}
                  </Text>
                  <Text style={styles.subContent}>
                    {farm.beanName}
                  </Text>
                </View>
              </View>
              <View>
                <Text style={{ marginRight: 10 }}>청약 진행 중</Text>
              </View>
            </Pressable>
          ))}
          <Pressable
            style={styles.itemStyle}
            onPress={() => Navigation.navigate("farmList")}
          >
            <View style={styles.itemContentStyle}>
              <View style={{ backgroundColor: "#F8F8F8", height: 40, width: 40, borderRadius: 25, alignItems: "center", justifyContent: "center", marginLeft: 10 }}>
                <Ionicons name="add" size={24} color="#5492F7" />
              </View>
              <Text style={{ marginLeft: 20, color: "#BBBBBB", fontFamily: "pretendard-bold", fontSize: 16 }}>
                추가하기
              </Text>
            </View>
          </Pressable>
        </View>
      </View> :
        <View>
          <Text
            style={styles.header}
          >관심 농장</Text>
          <View
            style={styles.container}
          >
            <Image
              source={require("../../../assets/images/farmfarm.png")}
              style={styles.ImageStyle}
            />
            <Text
              style={styles.content}
            >수익 난 농장들을 구경해보세요</Text>
            <Text
              style={styles.subContent}
            >1분이면 충분해요</Text>
            <View
              style={styles.btnStyle}
            >
              <Pressable
                onPress={() => Navigation.navigate("farmList")}>
                <Text
                  style={styles.btnTextStyle}
                >찾아 보기</Text>
              </Pressable>
            </View>
          </View>
        </View>}
    </View>
  );
};

export default FarmILike;

const styles = StyleSheet.create({
  screen: {
    margin: 10
  },
  container: {
    alignItems: "center"
  },
  header: {
    fontFamily: "pretendard-extraBold",
    fontSize: 23,
    margin: 10
  },
  ImageStyle: {
    width: width / 3,
    height: width / 3,
    resizeMode: "contain"
  },
  content: {
    fontFamily: "pretendard-bold",
    fontSize: 16,
  },
  subContent: {
    fontFamily: "pretendard-regular",
    fontSize: 12,
    color: "grey"
  },
  btnStyle: {
    width: width - 50,
    margin: 20,
    padding: 10,
    borderRadius: 10,
    alignItems: "center",
    backgroundColor: "#3C80D0",
  },
  btnTextStyle: {
    fontFamily: "pretendard-regular",
    color: "white",
    fontSize: 18,
  },
  headContainer: {
    flexDirection: "row",
    alignItems: "center",
    justifyContent: "space-between"
  },
  editBtnStyle: {
    margin: 10,
    fontFamily: "pretendard-regular",
    fontSize: 16,
  },
  itemStyle: {
    marginBottom: 10,
    flexDirection: "row",
    alignItems: "center",
    justifyContent: "space-between",
    width: width * 0.9
  },
  itemContentStyle: {
    marginHorizontal: 10,
    flexDirection: "row",
    alignItems: "center",
    justifyContent: "space-between"
  }
});
