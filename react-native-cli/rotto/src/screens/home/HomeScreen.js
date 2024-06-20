import {
  View,
  Text,
  StyleSheet,
  ScrollView,
  Button,
  Platform,
  Image,
  BackHandler,
  Alert,
} from "react-native";

import MyHeader from "../../components/common/MyHeader";
import MyBanner from "../../components/home/MyBanner";
import MyDeposit from "../../components/home/MyDeposit";
import FaqBanner from "../../components/home/FaqBanner";
import FarmILike from "../../components/home/FarmILike";
import FarmNews from "../../components/home/FarmNews";
import { useEffect } from "react";
import { getAlertList } from "../../utils/AlertApi";
import { useDispatch } from "react-redux";
import { setAlertList } from "../../stores/alertSlice";

const HomeScreen = ({ navigation }) => {
  const dispatch = useDispatch();

  const getAlert = async () => {
    const res = await getAlertList()
    // console.log("getAlert1", res)
    dispatch(setAlertList(res.alerts))

  }
  useEffect(() => {
    getAlert()

    BackHandler.addEventListener('hardwareBackPress', () => {
      if (navigation.isFocused()) {
        Alert.alert('앱 종료', '앱을 종료하시겠습니까?', [
          { text: '취소', onPress: () => null},
          { text: '종료', onPress: () => BackHandler.exitApp() }
        ]);
        return true;
      }
    });

    return () => {
      BackHandler.removeEventListener();
    }
  }, [])

  return (
    <MyHeader>
      <ScrollView style={styles.container}>
        <MyBanner />
        <MyDeposit />
        <FaqBanner />
        <View style={styles.infoContainer}>
          <FarmILike />
          <FarmNews />
        </View>
      </ScrollView>
    </MyHeader>
  );
};

export default HomeScreen;

const styles = StyleSheet.create({
  container: {
    flex: 1,
  },
  infoContainer: {
    flex: 1,
    marginTop: 25,
    padding: 15,
    borderTopRightRadius: 15,
    borderTopLeftRadius: 15,
    backgroundColor: "white",
  },
});
