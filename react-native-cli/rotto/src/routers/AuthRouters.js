import { createStackNavigator } from "@react-navigation/stack";
import Routers from "./Routers";
import OnboardingScreen from "../screens/loading/OnboardingScreen";
import NameIdInputScreen from "../screens/user/NameIdInputScreen";
import PhoneNumberInputScreen from "../screens/user/PhoneNumberInputScreen";
import PasswordInputScreen from "../screens/user/PasswordInputScreen";
import SignInScreen from "../screens/user/SignInScreen";
import SplashScreen from "../screens/loading/SplashScreen";
import PINSettingScreen from "../screens/user/PINSettingScreen";
import PINInputScreen from "../screens/user/PINInputScreen";
import BioAuthScreen from "../screens/user/BioAuthScreen";
import EmailInputScreen from "../screens/user/EmailInputScreen";

const AuthRouters = () => {
  const AuthStack = createStackNavigator();

  return (
    // <AuthStack.Navigator screenOptions={{ headerShown: false }} >
    <AuthStack.Navigator
      screenOptions={{ headerShown: false }}
      initialRouteName="Splash"
    >
      <AuthStack.Screen name="Splash" component={SplashScreen} />
      <AuthStack.Screen name="Onboarding" component={OnboardingScreen} />
      <AuthStack.Screen name="NameIdInput" component={NameIdInputScreen} />
      <AuthStack.Screen
        name="PhoneNumberInput"
        component={PhoneNumberInputScreen}
      />
      <AuthStack.Screen name="PasswordInput" component={PasswordInputScreen} />
      <AuthStack.Screen name="EmailInput" component={EmailInputScreen} />
      <AuthStack.Screen name="PINSetting" component={PINSettingScreen} />
      <AuthStack.Screen name="SignIn" component={SignInScreen} />
      <AuthStack.Screen name="PINInput" component={PINInputScreen} />
      <AuthStack.Screen name="BioAuth" component={BioAuthScreen} />
      <AuthStack.Screen name="Routers" component={Routers} />
    </AuthStack.Navigator>
  );
};

export default AuthRouters;
