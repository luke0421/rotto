import { Pressable, StyleSheet, Text } from "react-native";
import { Ionicons } from "@expo/vector-icons";
import Colors from "../../constants/Colors";

const FilterButton = ({ filterName, onPress, isChecked }) => {
  return (
    <Pressable
      style={
        isChecked
          ? [styles.filterButton, styles.checkedFilterButton]
          : styles.filterButton
      }
      onPress={onPress}
    >
      <Text style={isChecked ? styles.checkedFilterFont : styles.filterFont}>{filterName}</Text>
    </Pressable>
  );
};

export default FilterButton;

const styles = StyleSheet.create({
  filterButton: {
    flexDirection: "row",
    alignItems: "center",
    justifyContent: "center",
    height: 35,
    borderRadius: 5,
    backgroundColor: "white",
    borderColor: Colors.borderGray,
    borderWidth: 1,
    marginLeft: 10,
  },
  checkedFilterButton:{
    borderColor: "black",
    borderWidth: 1,
  },
  filterFont: {
    marginBottom: 3,
    marginHorizontal: 10,
    fontFamily: "pretendard-regular",
    fontSize: 15,
  },
  checkedFilterFont: {
    marginBottom: 3,
    marginHorizontal: 10,
    fontFamily: "pretendard-bold",
    fontSize: 15,
  },
});
