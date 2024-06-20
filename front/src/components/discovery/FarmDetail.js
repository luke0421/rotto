import { useEffect, useState } from "react";
import {
  Image,
  Pressable,
  StyleSheet,
  Text,
  View,
  useWindowDimensions,
} from "react-native";
import Colors from "../../constants/Colors";
import { useNavigation } from "@react-navigation/native";

const FarmDetail = ({ data }) => {
  const Navigation = useNavigation();
  const { width, height } = useWindowDimensions();
  const [error, setError] = useState(false);

  useEffect(() => {
    // console.log(data);
  }, []);

  return (
    <Pressable
      onPress={() => {
        Navigation.navigate("farm", {
          farmCode: data.farmCode,
        });
      }}
      style={[styles.container, { width: width * 0.9, height: 90 }]}
    >
      <Image
        source={
          error
            ? require("../../../assets/images/farmProfile2.png")
            : {
                uri: `${process.env.EXPO_PUBLIC_S3URL}/farm_img/${data.farmCode}/${data.farmLogoPath}`,
              }
        }
        onError={() => setError(true)}
        style={styles.farmLogo}
      />
      <View>
        <View style={[styles.rowContainer, { width: "90%" }]}>
          <View style={styles.rowItem}>
            <Text style={styles.title}>{data.farmName}</Text>
          </View>
          <View style={styles.rowItem}>
            <Text
              style={
                data.returnRate > 0
                  ? [styles.infoContent, styles.returnRatePlus]
                  : [styles.infoContent, styles.returnRateMinus]
              }
            >
              지난 수익률 : {data.returnRate > 0 ? "+" : "-"}
              {data.returnRate}%
            </Text>
            <Text style={styles.infoContent}>원두 : {data.beanName}</Text>
          </View>
        </View>
      </View>
    </Pressable>
  );
};

export default FarmDetail;

const styles = StyleSheet.create({
  container: {
    marginBottom: 0,
    flexDirection: "row",
    alignItems: "center",
  },
  title: {
    color: "black",
    fontFamily: "pretendard-semiBold",
    fontSize: 15
  },
  farmLogo: {
    height: 52,
    width: 52,
    resizeMode: "stretch",
    borderRadius: 26,
    marginRight: 10,
  },
  profit: {
    color: "#000000",
  },
  returnRatePlus: {
    color: "red",
  },
  returnRateMinus: {
    color: "blue",
  },
  rowContainer: {
    flexDirection: "row",
    justifyContent: "space-between",
  },
  rowItem: {
    justifyContent: "center",
    alignItems: "flex-end"
  },
  infoContent: {
    fontFamily: "pretendard-regular",
    fontSize: 13
  }
});
