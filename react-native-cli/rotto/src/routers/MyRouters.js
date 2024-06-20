import { createStackNavigator } from "@react-navigation/stack";
import MyScreen from "../screens/my/MyScreen";
import AccountScreen from "../screens/my/AccountScreen";
import ConnectionScreen from "../screens/my/ConnectionScreen";
import BioAuthScreen from "../screens/my/BioAuthScreen";
import PinAuthScreen from "../screens/my/PinAuthScreen";
import TransactionResultScreen from "../screens/my/TransactionResultScreen";
import TransactionScreen from "../screens/my/TransactionScreen";
import OwnDetailScreen from "../screens/my/OwnDetailScreen";


const MyRouters = () => {
  const MyStack = createStackNavigator();

  return (
    <MyStack.Navigator screenOptions={{ headerShown: false }}>
      <MyStack.Screen name="mypage" component={MyScreen} />
      <MyStack.Screen name="account" component={AccountScreen} />
      <MyStack.Screen name="connection" component={ConnectionScreen} />
      <MyStack.Screen name="transaction" component={TransactionScreen} />
      <MyStack.Screen name="transactionResult" component={TransactionResultScreen} />
      <MyStack.Screen name="transactionBioAuth" component={BioAuthScreen} />
      <MyStack.Screen name="transactionPinAuth" component={PinAuthScreen} />
      <MyStack.Screen name="ownDetail" component={OwnDetailScreen} />
    </MyStack.Navigator>
  );
};

export default MyRouters;
