import { useNavigation } from "@react-navigation/native";
import React, { useState, useRef } from "react";
import {
  ScrollView,
  View,
  Text,
  Dimensions,
  StyleSheet,
  Image,
  TouchableOpacity,
} from "react-native";

const { width } = Dimensions.get("window");

const MyBanner = () => {
  const Navigation = useNavigation();
  const [currentIndex, setCurrentIndex] = useState(0);
  const scrollViewRef = useRef();

  const handleScroll = (event) => {
    const contentOffsetX = event.nativeEvent.contentOffset.x;
    const newIndex = Math.round(contentOffsetX / width);
    setCurrentIndex(newIndex);
  };

  return (
    <View style={styles.carouselContainer}>
      <Text style={styles.header}>공지사항</Text>
      {/* <ScrollView
        horizontal={true}
        pagingEnabled={true}
        showsHorizontalScrollIndicator={false}
        style={styles.scrollViewStyle}
        onScroll={handleScroll}
        ref={scrollViewRef}
        scrollEventThrottle={16}
      > */}
        <TouchableOpacity onPress={() => Navigation.navigate("announcement")}>
          <View
            style={[
              styles.item,
              {
                backgroundColor: "#209FF9",
                flexDirection: "column",
                alignItems: "center",
                justifyContent: "space-between"
              },
            ]}
          >
            <View
              style={[
                {
                  flexDirection: "row",
                  alignItems: "center",
                  justifyContent: "space-between",
                  paddingTop: 20,
                },
              ]}
            >
              <View
                style={[
                  {
                    flex: 1,
                    alignItems: "flex-start",
                    marginHorizontal: 30,
                  },
                ]}
              >
                <Text style={styles.textHeader}>청약 안내</Text>
                <Text style={styles.text}>5월 신청 농장</Text>
              </View>
              <Image
                source={require("../../../assets/images/farmfarm.png")}
                style={{width: width * 0.25, height:  width*0.25, resizeMode: "contain", flex:1 }}
              />
            </View>
            {/* <View style={styles.indicatorContainer}>
              {[...Array(3).keys()].map((index) => (
                <View
                  key={index}
                  style={[
                    styles.indicator,
                    currentIndex === index && styles.activeIndicator,
                  ]}
                />
              ))}
            </View> */}
          </View>
        </TouchableOpacity>

        {/* <TouchableOpacity onPress={() => Navigation.navigate("announcement")}>
          <View
            style={[
              styles.item,
              {
                backgroundColor: "#C3A995",
                flexDirection: "column",
                alignItems: "center",
                justifyContent: "space-between",
              },
            ]}
          >
            <View
              style={[
                {
                  flexDirection: "row",
                  alignItems: "center",
                  justifyContent: "space-between",
                  paddingTop: 20,
                },
              ]}
            >
              <View
                style={[
                  {
                    alignItems: "start",
                    marginHorizontal: 30,
                  },
                ]}
              >
                <Text style={styles.textHeader}>타임라인</Text>
                <Text style={styles.text}>ROTTO 주요 정보</Text>
              </View>
              <Image
                source={require("../../../assets/images/farmfarm.png")}
                style={{width: width * 0.25, height:  width*0.25, resizeMode: "contain" }}
              />
            </View>
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
          </View>
        </TouchableOpacity>

        <TouchableOpacity onPress={() => Navigation.navigate("announcement")}>
          <View
            style={[
              styles.item,
              {
                backgroundColor: "#4E4E4E",
                flexDirection: "column",
                alignItems: "center",
                justifyContent: "space-between",
              },
            ]}
          >
            <View
              style={[
                {
                  flexDirection: "row",
                  alignItems: "center",
                  justifyContent: "space-between",
                  paddingTop: 20,
                },
              ]}
            >
              <View
                style={[
                  {
                    alignItems: "center",
                    marginHorizontal: 10,
                  },
                ]}
              >
                <Text style={styles.text}>커피농장 투자</Text>
                <Text style={styles.text}>시작해볼까요?</Text>
              </View>
              <Image
                source={require("../../../assets/images/farmfarm.png")}
                style={{
                  width: width * 0.25,
                  height:  width*0.25,
                  resizeMode: "contain",
                  marginLeft: 30,
                }}
              />
            </View>
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
          </View>
        </TouchableOpacity> */}
      {/* </ScrollView> */}
    </View>
  );
};

const styles = StyleSheet.create({
  header: {
    fontFamily: "pretendard-extraBold",
    fontSize: 18,
    marginHorizontal: 25,
    color: "white",
  },
  carouselContainer: {
    // 새로운 스타일
    marginTop: 10,
    position: "relative",
  },
  scrollViewStyle: {
    width: "100%",
  },
  item: {
    width: width * 0.9,
    borderRadius: 15,
    marginTop: 10,
    marginHorizontal: width * 0.05,
    height: 160,
    justifyContent: "center",
    alignItems: "center",
  },
  textHeader: {
    fontFamily: "pretendard-regular",
    fontSize: 18,
    color: "white",
  },
  text: {
    fontFamily: "pretendard-extraBold",
    fontSize: 22,
    color: "white",
  },
  indicatorContainer: {
    marginLeft: 10,
    position: "absolute",
    bottom: 10,
    left: 20, // 왼쪽 아래 정렬을 위해 변경
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

export default MyBanner;
