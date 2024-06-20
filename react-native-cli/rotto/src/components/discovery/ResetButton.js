import { Pressable, StyleSheet, Text } from "react-native";
import { Ionicons } from "@expo/vector-icons";
import Colors from "../../constants/Colors";

const ResetButton = ({ filterName, onPress, isChecked }) => {
  return (
    <Pressable
      style={
        isChecked
          ? [styles.resetButton, styles.checkedFilterButton]
          : styles.resetButton
      }
      onPress={onPress}
    >
      <Ionicons name="reload" size={20} color="black" />
      <Text style={isChecked ? styles.checkedFilterFont : styles.filterFont}>{filterName}</Text>
    </Pressable>
  );
};

export default ResetButton;

const styles = StyleSheet.create({
  resetButton: {
    flexDirection: "row",
    alignItems: "center",
    justifyContent: "center",
    height: 35,
    borderRadius: 5,
    backgroundColor: "white",
    borderColor: Colors.borderGray,
    borderWidth: 1,
    marginLeft: 10,
    paddingLeft: 10
  },
  filterFont: {
    marginBottom: 3,
    marginHorizontal: 10,
    fontFamily: "pretendard-regular",
    fontSize: 15,
  },
});
