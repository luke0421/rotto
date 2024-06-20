import { createStackNavigator } from "@react-navigation/stack";
import FundingScreen from "../screens/funding/FundingScreen";
import FundingDetailScreen from "../screens/funding/FundingDetailScreen";

const FundingRouters = () => {
  const FundingStack = createStackNavigator();

  return (
    <FundingStack.Navigator screenOptions={{ headerShown: false }}>
      <FundingStack.Screen name="funding" component={FundingScreen} />
      <FundingStack.Screen name="fundingDetail" component={FundingDetailScreen} />
    </FundingStack.Navigator>
  );
};

export default FundingRouters;
