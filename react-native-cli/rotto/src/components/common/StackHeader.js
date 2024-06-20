import { View, Text, StyleSheet, Image, Pressable } from "react-native";
import { Ionicons } from "@expo/vector-icons";
import { useNavigation } from "@react-navigation/native";

import Colors from "../../constants/Colors";

const StackHeader = ({
  title,
  screenName,
  color,
  search,
  onPress,
  detail,
  isLiked,
  onPressHeart,
  returnTo,
  empty
}) => {
  const Navigation = useNavigation();

  return (
    <View
      style={[
        styles.container,
        color ? { backgroundColor: color } : { backgroundColor: "white" },
      ]}
    >
      <View style={styles.iconContainer}>
        <Ionicons
          name="chevron-back"
          size={20}
          color={color ? "white" : "black"}
          onPress={() => Navigation.navigate(returnTo ? returnTo : screenName)}
        />
      </View>
      <View>
        <Text
          style={[
            styles.title,
            color ? { color: "white" } : { color: "black" },
          ]}
        >
          {title}
        </Text>
      </View>
      {search && (
        <Pressable style={styles.iconContainer} onPress={onPress}>
          <Ionicons name="search" size={20} color={color ? "white" : "black"} />
        </Pressable>
      )}
      {detail && (
        <Pressable style={styles.iconContainer} onPress={onPressHeart}>
          {isLiked ? (
            <Ionicons name="heart" size={20} color="red" />
          ) : (
            <Ionicons name="heart-outline" size={20} color="red" />
          )}
        </Pressable>
      )}
      {empty && <View style={{width: 50}}></View>}
    </View>
  );
};

export default StackHeader;

const styles = StyleSheet.create({
  container: {
    flexDirection: "row",
    backgroundColor: "white",
    height: 70,
    justifyContent: "space-between",
    alignItems: "center",
  },
  title: {
    fontFamily: "pretendard-bold",
    fontSize: 15,
  },
  iconContainer: {
    marginHorizontal: 15,
    flexDirection: "row",
    alignItems: "center",
    justifyContent: "space-between",
  },
});
