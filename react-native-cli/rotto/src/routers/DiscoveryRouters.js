import { createStackNavigator } from "@react-navigation/stack";
import DiscoveryScreen from "../screens/discovery/DiscoveryScreen";
import FarmListScreen from "../screens/discovery/FarmListScreen";
import FarmScreen from "../screens/discovery/FarmScreen";

const DiscoveryRouters = () => {
  const DiscoveryStack = createStackNavigator();

  return (
    <DiscoveryStack.Navigator screenOptions={{ headerShown: false }}>
      <DiscoveryStack.Screen name="discovery" component={DiscoveryScreen} />
      <DiscoveryStack.Screen name="farmList" component={FarmListScreen} />
      <DiscoveryStack.Screen name="farmDetail" component={FarmScreen} />
    </DiscoveryStack.Navigator>
  );
};

export default DiscoveryRouters;
