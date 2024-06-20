import {
  View,
  Button,
  StyleSheet,
  ScrollView,
  Text,
  Dimensions,
  Image,
  Pressable,
} from "react-native";
import { useNavigation } from "@react-navigation/native";
import React, { useState, useRef } from "react";

const { width } = Dimensions.get("window");

const OnboardingScreen = () => {
  const navigation = useNavigation();

  const [currentIndex, setCurrentIndex] = useState(0);
  const scrollViewRef = useRef();

  const handleScroll = (event) => {
    const contentOffsetX = event.nativeEvent.contentOffset.x;
    const newIndex = Math.round(contentOffsetX / width);
    setCurrentIndex(newIndex);
  };

  return (
    <View style={styles.screen}>
      <ScrollView
        horizontal={true}
        pagingEnabled={true}
        showsHorizontalScrollIndicator={false}
        style={styles.scrollViewStyle}
        onScroll={handleScroll}
        ref={scrollViewRef}
        scrollEventThrottle={16}
      >
        <View style={styles.item}>
          <View style={styles.itemText}>
            <View>
              <Text style={styles.itemTitle}>커피 한 잔처럼 쉽게 </Text>
              <Text style={styles.itemTitle}>농장 조각 구매</Text>
            </View>
            <View>
              <Text style={styles.itemContent}>
                부업으로 커피 농장주가 되세요
              </Text>
            </View>
          </View>
          <Image
            source={require("../../../assets/images/coffeefarmfarm.png")}
            style={[styles.itemImage, {marginRight: 30}]}
          />
        </View>
        <View style={styles.item}>
          <View style={styles.itemText}>
            <View>
              <Text style={styles.itemTitle}>돈은 농장이 벌어올거에요</Text>
            </View>
            <View>
              <Text style={styles.itemContent}>커피 농장 조각 하나만 사도</Text>
              <Text style={styles.itemContent}>자산이 차곡차곡!</Text>
              <Text style={styles.itemContent}>
                수익률이 높은 농장에 투자해보세요
              </Text>
            </View>
          </View>
          <Image
            source={require("../../../assets/images/farmfarmfarm.png")}
            style={[styles.itemImage, {}]}
          />
        </View>
        <View style={styles.item}>
          <View style={styles.itemText}>
            <View>
              <Text style={styles.itemTitle}>빠르고 간단한 조각 투자</Text>
            </View>
            <View>
              <Text style={styles.itemContent}>농장 조각을 주식처럼</Text>
              <Text style={styles.itemContent}>빠르고 안전하게 구매해요</Text>
            </View>
          </View>
          <Image
            source={require("../../../assets/images/coinfarmfarm.png")}
            style={[styles.itemImage, {marginTop: 20}]}
          />
        </View>
      </ScrollView>
      <View style={styles.indicatorContainer}>
        {[...Array(3).keys()].map((index) => (
          <View
            key={index}
            style={[
              styles.indicator,
              currentIndex === index && styles.activeIndicator,
            ]}
          />
        ))}
      </View>
      <View style={styles.btnContainer}>
        <Pressable onPress={() => navigation.navigate("NameIdInput")}>
          <View style={styles.btnStartStyle}>
            <Text style={styles.btnStartTextStyle}>ROTTO 시작하기</Text>
          </View>
        </Pressable>
        <Pressable onPress={() => navigation.navigate("SignIn")}>
          <View style={styles.btnAlreadyStyle}>
            <Text>이미 계정이 있어요</Text>
          </View>
        </Pressable>
      </View>
    </View>
  );
};

export default OnboardingScreen;

const styles = StyleSheet.create({
  screen: {
    flex: 1,
    alignItems: "center",
    justifyContent: "space-between",
  },
  item: {
    marginTop: 100,
    alignItems: "center",
    width: width,
  },
  itemText: {
    alignItems: "baseline",
    marginBottom: 40
  },
  itemTitle: {
    fontFamily: "pretendard-extraBold",
    fontSize: 28,
  },
  itemContent: {
    fontFamily: "pretendard-regular",
    fontSize: 18,
  },
  itemImage: {
    width: width * 0.7,
    height: width * 0.7,
    resizeMode: "contain",
  },
  btnContainer: {
    marginTop: 10,
    marginBottom: 50,
  },
  btnStartStyle: {
    backgroundColor: "grey",
    width: (width * 2) / 3,
    height: 50,
    borderRadius: 10,
    alignItems: "center",
    justifyContent: "center",
  },
  btnStartTextStyle: {
    fontFamily: "pretendard-bold",
    marginVertical: 10,
    fontSize: 20,
    color: "white",
  },
  btnAlreadyStyle: {
    margin: 10,
    alignItems: "center",
  },

  indicatorContainer: {
    bottom: 10,
    flexDirection: "row",
    justifyContent: "center",
    alignItems: "center",
  },
  indicator: {
    width: 10,
    height: 10,
    borderRadius: 5,
    backgroundColor: "lightgray",
    margin: 5,
  },
  activeIndicator: {
    backgroundColor: "white",
  },
});
