import { createMaterialBottomTabNavigator } from "@react-navigation/material-bottom-tabs";
import { Ionicons } from "@expo/vector-icons";

import HomeRouters from "./HomeRouters";
import DiscoveryRouters from "./DiscoveryRouters";
import FundingRouters from "./FundingRouters";
import MyRouters from "./MyRouters";
import SettingRouters from "./SettingRouters";
import Colors from "../constants/Colors";

const Routers = () => {
  const Tab = createMaterialBottomTabNavigator();

  return (
    <Tab.Navigator
      barStyle={{
        backgroundColor: "white",
        paddingHorizontal: 10,
        borderColor: Colors.borderGray,
        borderTopWidth: 1,
      }}
      initialRouteName="HomePage"
    >
      <Tab.Screen
        name="홈"
        component={HomeRouters}
        options={{
          tabBarIcon: () => (
            <Ionicons name="home-outline" size={24} color="black" />
          ),
        }}
      />
      <Tab.Screen
        name="발견"
        component={DiscoveryRouters}
        options={{
          tabBarIcon: () => (
            <Ionicons name="compass-outline" size={24} color="black" />
          ),
        }}
      />
      <Tab.Screen
        name="청약"
        component={FundingRouters}
        options={{
          tabBarIcon: () => (
            // <Ionicons name="cash-outline" size={24} color="black" />
            <Ionicons name="extension-puzzle-outline" size={24} color="black" />
          ),
        }}
      />
      <Tab.Screen
        name="My"
        component={MyRouters}
        options={{
          tabBarIcon: () => (
            <Ionicons name="person-outline" size={24} color="black" />
          ),
        }}
      />
      <Tab.Screen
        name="설정"
        component={SettingRouters}
        options={{
          tabBarIcon: () => (
            <Ionicons name="settings-outline" size={24} color="black" />
          ),
        }}
      />
    </Tab.Navigator>
  );
};

export default Routers;
