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
          <Top10ListItem data={item} index={index} key={item.farmCode} />
          // return (
          //   <View key={item.farmCode} style={styles.listItem}>
          //     <View style={styles.imageContainer}>
          //       <Image
          //         source={require("../../../assets/images/coffee-bean-logo.png")}
          //         style={{resizeMode: "stretch", width: 30, height: 30}}
          //       />
          //     </View>
          //     <View>
          //       <Text style={styles.textNormal}>
          //         <Text style={styles.textEmp}>{item.farmName}</Text> ( 원두:{" "}
          //         {item.beanName} )
          //       </Text>
          //       <Text>
          //         지난 수익률 : <Text>+ 10.0 %</Text>
          //       </Text>
          //     </View>
          //   </View>
          // );
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
