import { View, StyleSheet } from "react-native";
import MyHeader from "../../components/common/MyHeader";
import MyWallet from "../../components/my/MyWallet";
import MyAccount from "../../components/my/MyAccount";
import { useDispatch } from "react-redux";
import { setConnectedAccount, setDisconnectModal } from "../../stores/mySlice";
import { useCallback } from "react";
import { useFocusEffect } from "@react-navigation/native";
import InvestmentList from "../../components/my/InvestmentList";
import { getRealAccountInfo } from "../../utils/accountApi";

const MyScreen = ({navigation}) => {
  const dispatch = useDispatch();

  useFocusEffect(
    useCallback(() => {
      const getRealAccountData = async () => {
        const res = await getRealAccountInfo();
        dispatch(setConnectedAccount(res));
      };

      getRealAccountData();

      return () => {
        dispatch(setDisconnectModal(false));
      }
    }, [navigation])
  );

  return (
    <MyHeader>
      <View style={styles.container}>
        <MyAccount navigation={navigation} />
        <MyWallet />
        <InvestmentList navigation={navigation} />
      </View>
    </MyHeader>
  );
};

export default MyScreen;

const styles = StyleSheet.create({
  container: {
    alignItems: 'center',
    marginTop: 28,
    gap: 20,
    flex: 1
  }
})