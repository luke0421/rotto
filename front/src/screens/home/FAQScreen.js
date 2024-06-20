import { useNavigation } from "@react-navigation/native";
import { useEffect, useState } from "react";
import {
  FlatList,
  Pressable,
  ScrollView,
  StyleSheet,
  View,
} from "react-native";
import { Ionicons } from "@expo/vector-icons";
import { Text } from "react-native-paper";
import { getFAQList } from "../../utils/FAQApi";
import Colors from "../../constants/Colors";

const FAQScreen = () => {
  const [faqList, setFaqList] = useState([]);
  const Navigation = useNavigation();

  const getList = async () => {
    const res = await getFAQList();
    //console.log(res);
    setFaqList(res.faqListDtos);
  };

  useEffect(() => {
    getList();
  }, []);

  const [clickedStates, setClickedStates] = useState({});

  const rendeFAQList = (itemData) => {
    const isClicked = clickedStates[itemData.item.faqCode];

    return (
      <View>
        <View style={styles.container}>
          <Text style={styles.qText}>Q</Text>
          <Text style={styles.title}>{itemData.item.title}</Text>
          <Ionicons
            style={styles.myIconStyle}
            name={isClicked ? "chevron-up" : "chevron-down"}
            size={22}
            onPress={() =>
              setClickedStates({
                ...clickedStates,
                [itemData.item.faqCode]: !clickedStates[itemData.item.faqCode],
              })
            }
          />
        </View>
        {clickedStates[itemData.item.faqCode] && (
        <View
        style={[
          {
            backgroundColor: "#EFEFEF"
          },
        ]}
        >
          <Text style={styles.myContent}>
          {itemData.item.content}
          </Text>
        </View>)}
      </View>


    );
  };

  return (
    <View style={styles.screen}>
      <Pressable
        style={styles.iconContainer}
        onPress={() => Navigation.navigate("home")}
      >
        <Ionicons name="chevron-back" size={30} color={Colors.iconGray} />
      </Pressable>
      <View
        style={[
          {
            marginTop: 10,
            flexDirection: "row",
            alignItems: "center",
            justifyContent: "center",
          },
        ]}
      >
        <Text style={styles.header}>FAQ</Text>
      </View>
      <FlatList
        data={faqList}
        keyExtractor={(item, index) => index.toString()}
        renderItem={rendeFAQList}
      />
    </View>
  );
};

export default FAQScreen;

const styles = StyleSheet.create({
  container: {
    flexDirection: "row",
    alignContent: "center",
    margin: 10,
  },
  screen: {
    flex: 1,
    backgroundColor: "white",
  },
  header: {
    fontFamily: "pretendard-extraBold",
    fontSize: 26,
    paddingBottom: 20,
  },
  title: {
    fontFamily: "pretendard-regular",
    fontSize: 16,
    margin: 10,
    alignContent: "stretch",
    justifyContent: "center",
  },
  content: {
    marginTop: 5,
  },
  iconContainer: {
    margin: 15,
    position: "absolute",
    justifyContent: "flex-start",
    alignItems: "center",
  },
  qText: {
    alignItems: "center",
    justifyContent: "center",
    fontSize: 16,
    color: "grey",
    margin: 10,
  },
  myIconStyle: {
    margin: 10,
    color: "grey",
    justifyContent: "center",
    alignContent: "center",
  },
  myContent:{
    marginHorizontal: 40,
    marginVertical: 5,
    padding:10,
  }
});
