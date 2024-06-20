import { createStackNavigator } from "@react-navigation/stack";
import HomeScreen from "../screens/home/HomeScreen";
import AnnouncementScreen from "../screens/home/AnnouncementScreen";
import NoticeScreen from "../screens/home/NoticeScreen";
import AlertScreen from "../screens/home/AlertScreen";
import AlertListScreen from "../screens/home/AlertListScreen";
import FAQScreen from "../screens/home/FAQScreen";
import FarmILikeListScreen from "../screens/home/FarmILikeScreen";
import NewsWebview from "../components/home/NewsWebview";
import NewsScreen from "../screens/home/NewsScreen";

const HomeRouters = () => {
  const HomeStack = createStackNavigator();

  return (
    <HomeStack.Navigator screenOptions={{ headerShown: false }}>
      <HomeStack.Screen name="home" component={HomeScreen} />
      <HomeStack.Screen name="announcement" component={AnnouncementScreen} />
      <HomeStack.Screen name="notice" component={NoticeScreen} />
      <HomeStack.Screen name="alertList" component={AlertListScreen} />
      <HomeStack.Screen name="alert" component={AlertScreen} />
      <HomeStack.Screen name="faqscreen" component={FAQScreen} />
      <HomeStack.Screen name="farmILikeScreen" component={FarmILikeListScreen} />
      <HomeStack.Screen name="NewsWebview" component={NewsWebview} />
      <HomeStack.Screen name="newsList" component={NewsScreen} />

    </HomeStack.Navigator>
  );
};

export default HomeRouters;
