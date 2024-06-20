import React, { useState, useRef } from "react";
import { ScrollView, View, Text, Dimensions, StyleSheet } from "react-native";

const { width } = Dimensions.get("window");

const MyCarousel = () => {
  const [currentIndex, setCurrentIndex] = useState(0);
  const scrollViewRef = useRef();

  const handleScroll = (event) => {
    const contentOffsetX = event.nativeEvent.contentOffset.x;
    const newIndex = Math.round(contentOffsetX / width);
    setCurrentIndex(newIndex);
  };

  return (
    <View style={styles.carouselContainer}>
      <Text style={styles.header}>관심 농장</Text>
      <ScrollView
        horizontal={true}
        pagingEnabled={true}
        showsHorizontalScrollIndicator={false}
        style={styles.scrollViewStyle}
        onScroll={handleScroll}
        ref={scrollViewRef}
        scrollEventThrottle={16}
      >
        <View style={[styles.item, { backgroundColor: "#209FF9" }]}>
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
          <Text style={styles.text}>Item 1</Text>
        </View>
        <View style={[styles.item, { backgroundColor: "#C3A995" }]}>
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
          <Text style={styles.text}>Item 2</Text>
        </View>
        <View style={[styles.item, { backgroundColor: "#4E4E4E" }]}>
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
          <Text style={styles.text}>Item 3</Text>
        </View>
      </ScrollView>
    </View>
  );
};

const styles = StyleSheet.create({
  header: {
    fontFamily: "pretendard-extraBold",
    fontSize: 23,
    paddingBottom: 20,
  },
  carouselContainer: {
    // 새로운 스타일
    marginTop: 50,
    position: "relative",
  },
  scrollViewStyle: {
    width: "100%",
  },
  item: {
    width: width - 40,
    borderRadius: 15,
    margin: 20,
    height: 200,
    justifyContent: "center",
    alignItems: "center",
  },
  text: {
    fontSize: 20,
    color: "white",
  },
  indicatorContainer: {
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

export default MyCarousel;
