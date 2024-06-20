import {
  Button,
  Dimensions,
  Pressable,
  ScrollView,
  StyleSheet,
  Text,
  View,
} from "react-native";
import MyHeader from "../../components/common/MyHeader";
import FarmPreview from "../../components/discovery/FarmPreview";
import CoffeeInfo from "../../components/discovery/CoffeeInfo";
import FarmTOP10 from "../../components/discovery/FarmTOP10";
import { useNavigation } from "@react-navigation/native";
import Colors from "../../constants/Colors";

const { width } = Dimensions.get("window");

const DiscoveryScreen = () => {
  const Navigation = useNavigation();

  return (
    <>
      <MyHeader>
        <ScrollView style={styles.container}>
          <View style={styles.component}>
            <View style={styles.rowContainer}>
              <Text style={styles.title}>농장</Text>
              <Text
                style={styles.navigateText}
                onPress={() => Navigation.navigate("farmList")}
              >
                농장 조회
              </Text>
            </View>
            <FarmPreview />
          </View>
          <View style={styles.component}>
            <Text style={styles.title}>원두소개</Text>
            <CoffeeInfo />
          </View>
          <View style={styles.borderComponent}>
            <Text style={styles.titleBlack}>이전 청약 수익률 TOP10</Text>
            <FarmTOP10 />
          </View>
        </ScrollView>
      </MyHeader>
    </>
  );
};

export default DiscoveryScreen;

const styles = StyleSheet.create({
  container: {
    flex: 1,
    marginTop: 10,
    paddingVertical: 25,
    backgroundColor: Colors.bgOrg,
  },
  title: {
    color: "white",
    fontFamily: "pretendard-bold",
    fontSize: 20,
  },
  titleBlack: {
    color: "black",
    fontFamily: "pretendard-bold",
    fontSize: 20,
    marginBottom: 20
  },
  component: {
    marginHorizontal: 15,
    flex: 1,
    marginBottom: 20,
  },
  navigateText: {
    color: "white",
    fontFamily: "pretendard-bold",
    fontSize: 15,
  },
  rowContainer: {
    flexDirection: "row",
    justifyContent: "space-between"
  },
  borderComponent: {
    flex: 1,
    padding: 25,
    marginBottom: 20,
    backgroundColor: "white",
    borderTopLeftRadius: 15,
    borderTopRightRadius: 15,
  }
});
