import { useEffect, useState } from "react";
import { Dimensions, Pressable, StyleSheet, Text, View } from "react-native";
import {
  Gesture,
  GestureDetector,
  PanGestureHandler,
} from "react-native-gesture-handler";
import Animated, {
  useAnimatedStyle,
  useSharedValue,
  withSpring,
} from "react-native-reanimated";
import Colors from "../../constants/Colors";

const { height: SCREEN_HEIGHT } = Dimensions.get("window");
const MAX_TRANSLATE_Y = SCREEN_HEIGHT / 1.5;
const MIN_TRANSLATE_Y = SCREEN_HEIGHT / 5;

const MyBottomSheet = ({ children, isOpen, onGestureEvent }) => {
  const translateY = useSharedValue(0);
  // const context = useSharedValue({ y: 0 });

  // const gesture = Gesture.Pan()
  //   .onStart((e) => {
  //     translateY.value = e.translationY + context.value.y;
  //     translateY.value = Math.max(translateY.value, -MAX_TRANSLATE_Y);
  //   })
  //   .onEnd((e) => {
  //     if (translateY.value > -MIN_TRANSLATE_Y) {
  //       translateY.value = withSpring(SCREEN_HEIGHT);
  //     }
  //     if (translateY.value < -MIN_TRANSLATE_Y) {
  //       translateY.value = withSpring(-MAX_TRANSLATE_Y);
  //     }
  //   });

  const reanimatedBottomStyle = useAnimatedStyle((e) => {
    return {
      transform: [{ translateY: translateY.value }],
    };
  });

  // const scrollTo = (destination) => {
  //   "worklet";
  //   translateY.value = withSpring(destination, { damping: 50 });
  // };
  useEffect(() => {
    translateY.value = withSpring(isOpen ? -SCREEN_HEIGHT / 3 : SCREEN_HEIGHT);
  }, [isOpen, translateY]);
  // useEffect(() => {
  //   // scrollTo(-SCREEN_HEIGHT / 3);
  //   scrollTo(isOpen ? -SCREEN_HEIGHT / 2 : SCREEN_HEIGHT);
  // }, [isOpen]);

  return (
    <>
      {/* <GestureDetector gesture={gesture}> */}
      <PanGestureHandler onGestureEvent={onGestureEvent}>
        <Animated.View
          style={[styles.bottomsheet_container, reanimatedBottomStyle]}
        >
          <View style={styles.line}></View>
          {children}
        </Animated.View>
      </PanGestureHandler>
      {/* </GestureDetector> */}
      <Pressable pointerEvents="box-none" style={styles.overlay} />
    </>
  );
};

export default MyBottomSheet;

const styles = StyleSheet.create({
  bottomsheet_container: {
    width: "100%",
    height: SCREEN_HEIGHT,
    backgroundColor: "white",
    position: "absolute",
    top: SCREEN_HEIGHT / 1.5,
    // zIndex: 12000,
    borderRadius: 25,
    paddingHorizontal: 10,
    borderColor: Colors.borderGray,
    borderWidth: 2,
  },
  overlay: {
    ...StyleSheet.absoluteFillObject,
    backgroundColor: "transparent",
  },
  line: {
    width: 75,
    height: 4,
    backgroundColor: "#000000",
    borderRadius: 20,
    alignSelf: "center",
    marginVertical: 10,
  },
});
