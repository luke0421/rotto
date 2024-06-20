import { useEffect, useState } from "react";
import { Image, StyleSheet, Text, View } from "react-native";
import { getFarmTOP10 } from "../../utils/farmAPi";
import { useNavigation } from "@react-navigation/native";
import FarmDetail from "./FarmDetail";
import Top10ListItem from "./Top10ListItem";

const FarmTOP10 = () => {
  const [farmsTOP, setFarmsTOP] = useState([]);
  const Navigation = useNavigation();

  const getList = async () => {
    const res = await getFarmTOP10();
    setFarmsTOP(res.farms);
    //console.log(res)
  };

  useEffect(() => {
    getList();
  }, []);
  return (
    <View style={styles.container}>
      {farmsTOP &&
        farmsTOP.map((item, index) => 
          <Top10ListItem key={item.farmCode} data={item} index={index} />
        )}
    </View>
  );
};

export default FarmTOP10;

const styles = StyleSheet.create({
  container: {
    flex: 1,
  },
  listItem: {
    flexDirection: "row",
    marginBottom: 10,
  },
  textNormal: {
    fontFamily: "pretendard-regular",
    fontSize: 15,
  },
  textEmp: {
    fontFamily: "pretendard-bold",
  },
  imageContainer: {
    width: 30,
  },
});
