import { createStackNavigator } from "@react-navigation/stack";
import SettingScreen from "../screens/setting/SettingScreen";
import InquiryListScreen from './../screens/setting/InquiryListScreen';
import InquiryCreateScreen from "../screens/setting/InquiryCreateScreen";
import PINChangeScreen from "../screens/setting/PINChangeScreen";
import InquiryDetailScreen from "../screens/setting/InquiryDetailScreen";
import TermsScreen from "../screens/setting/TermsScreen";
import BioAuthScreen from "../screens/setting/BioAuthScreen";
import PinAuthScreen from "../screens/setting/PinAuthScreen";
import PasswordChangeScreen from "../screens/setting/PasswordChangeScreen";
import { useFocusEffect } from "@react-navigation/native";
import { useCallback } from "react";

const SettingRouters = ({navigation}) => {
  const SettingStack = createStackNavigator();

  useFocusEffect(
    useCallback(() => {
      return () => {
        navigation.reset({
          index: 1,
          routes:[{ name: 'setting' }]
        })
      }
    }, [])
  )

  return (
    <SettingStack.Navigator screenOptions={{ headerShown: false }}>
      <SettingStack.Screen name="setting" component={SettingScreen} />
      <SettingStack.Screen name="inquiry" component={InquiryListScreen} />
      <SettingStack.Screen name="inquiryCreate" component={InquiryCreateScreen} />
      <SettingStack.Screen name="inquiryDetail" component={InquiryDetailScreen} />
      <SettingStack.Screen name="pinChange" component={PINChangeScreen} />
      <SettingStack.Screen name="terms" component={TermsScreen} />
      <SettingStack.Screen name="bioAuth" component={BioAuthScreen} />
      <SettingStack.Screen name="pinAuth" component={PinAuthScreen} />
      <SettingStack.Screen name="passwordChange" component={PasswordChangeScreen} />
    </SettingStack.Navigator>
  )
}

export default SettingRouters;